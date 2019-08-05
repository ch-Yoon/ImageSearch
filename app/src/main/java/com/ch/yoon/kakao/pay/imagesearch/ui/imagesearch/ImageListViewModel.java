package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Document;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Meta;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper.ImageSearchInspector;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageListViewModel extends BaseViewModel {

    private static final String TAG = ImageListViewModel.class.getName();
    private static final ImageSortType DEFAULT_IMAGE_SORT_TYPE = ImageSortType.ACCURACY;
    private static final int DEFAULT_COUNT_OF_ITEM_IN_LINE = 3;

    @NonNull
    private final ImageRepository imageRepository;
    @NonNull
    private final ImageSearchInspector imageSearchInspector;

    @NonNull
    private final MutableLiveData<Integer> countOfItemInLineLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<Document>> imageInfoListLiveData = new MutableLiveData<>();
    @NonNull
    private final SingleLiveEvent<String> messageLiveData = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<ImageSearchState> imageSearchStateLiveEvent = new SingleLiveEvent<>();

    private boolean remainingDataOnRepository = true;

    public ImageListViewModel(@NonNull Application application,
                              @NonNull ImageRepository imageRepository,
                              @NonNull ImageSearchInspector imageSearchInspector) {
        super(application);
        this.imageRepository = imageRepository;
        this.imageSearchInspector = imageSearchInspector;

        init();
    }

    private void init() {
        countOfItemInLineLiveData.setValue(DEFAULT_COUNT_OF_ITEM_IN_LINE);
        imageSearchStateLiveEvent.setValue(ImageSearchState.NONE);
        observeImageSearchApprove();
    }

    @NonNull
    public LiveData<Integer> observeCountOfItemInLine() {
        return countOfItemInLineLiveData;
    }

    @NonNull
    public LiveData<List<Document>> observeImageInfoList() {
        return imageInfoListLiveData;
    }

    @NonNull
    public SingleLiveEvent<String> observeMessage() {
        return messageLiveData;
    }

    @NonNull
    public SingleLiveEvent<ImageSearchState> observeImageSearchState() {
        return imageSearchStateLiveEvent;
    }

    public void changeCountOfItemInLine(int countOfItemInLine) {
        countOfItemInLineLiveData.setValue(countOfItemInLine);
    }

    public void loadImageList(@NonNull String keyword) {
        imageInfoListLiveData.setValue(null);
        imageSearchInspector.submitFirstImageSearchRequest(keyword, DEFAULT_IMAGE_SORT_TYPE);
    }

    public void loadMoreImageListIfPossible(int displayPosition) {
        if(remainingDataOnRepository) {
            final List<Document> documentList = imageInfoListLiveData.getValue();
            if (documentList != null) {
                imageSearchInspector.submitPreloadRequest(
                    displayPosition,
                    documentList.size(),
                    DEFAULT_IMAGE_SORT_TYPE,
                    getCountOfItemInLine()
                );
            }
        }
    }

    public void retryLoadMoreImageList() {
        imageSearchInspector.submitRetryRequest(DEFAULT_IMAGE_SORT_TYPE);
    }

    private int getCountOfItemInLine() {
        Integer countOfItemInLine = countOfItemInLineLiveData.getValue();
        if(countOfItemInLine == null) {
            countOfItemInLine = DEFAULT_COUNT_OF_ITEM_IN_LINE;
        }

        return countOfItemInLine;
    }

    private void observeImageSearchApprove() {
        imageSearchInspector.observeImageSearchApprove(this::requestImageSearchToRepository);
    }

    private void requestImageSearchToRepository(ImageSearchRequest imageSearchRequest) {
        registerDisposable(
            imageRepository.requestImageList(imageSearchRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlingReceivedResponse, this::handlingError)
        );
    }

    private void handlingReceivedResponse(ImageSearchResponse imageSearchResponse) {
        changeImageSearchState(ImageSearchState.SUCCESS);

        updateMetaInfo(imageSearchResponse.getMeta());
        updateImageInfoList(imageSearchResponse.getDocumentList());
    }

    private void updateMetaInfo(Meta meta) {
        if(meta != null) {
            remainingDataOnRepository = !meta.isEnd();
        } else {
            remainingDataOnRepository = false;
        }
    }

    private void updateImageInfoList(List<Document> receivedDocumentList) {
        final List<Document> previousDocumentList = imageInfoListLiveData.getValue();

        if(CollectionUtil.isEmpty(receivedDocumentList)) {
            if(CollectionUtil.isEmpty(previousDocumentList)) {
                updateMessage(R.string.success_image_search_no_result);
            } else {
                updateMessage(R.string.success_image_search_last_data);
            }
        } else {
            List<Document> newDocumentList;
            if(CollectionUtil.isEmpty(previousDocumentList)) {
                newDocumentList = receivedDocumentList;
            } else {
                newDocumentList = new ArrayList<>(previousDocumentList);
                newDocumentList.addAll(receivedDocumentList);
            }

            imageInfoListLiveData.setValue(newDocumentList);
        }
    }

    private void handlingError(Throwable throwable) {
        changeImageSearchState(ImageSearchState.FAIL);

        if(throwable instanceof ImageSearchException) {
            final int messageResourceId = ((ImageSearchException) throwable).getErrorMessageResourceId();
            updateMessage(messageResourceId);
        }

        Log.d(TAG, throwable.getMessage());
    }

    private void updateMessage(@StringRes int stringResourceId) {
        final String message = getString(stringResourceId);
        messageLiveData.setValue(message);
    }

    private void changeImageSearchState(ImageSearchState imageSearchState) {
        ImageSearchState previousImageSearchState = imageSearchStateLiveEvent.getValue();
        if(previousImageSearchState != imageSearchState) {
            imageSearchStateLiveEvent.setValue(imageSearchState);
        }
    }

}
