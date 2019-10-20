package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.SearchMetaInfo;
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.request.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchError;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;
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
    private final MutableLiveData<ImageSortType> imageSortTypeLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<Integer> countOfItemInLineLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<ImageDocument>> imageDocumentListLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<ImageSearchState> imageSearchStateLiveData = new MutableLiveData<>();

    @Nullable
    private SearchMetaInfo searchMetaInfo;

    ImageListViewModel(@NonNull Application application,
                       @NonNull ImageRepository imageRepository,
                       @NonNull ImageSearchInspector imageSearchInspector) {
        super(application);
        this.imageRepository = imageRepository;
        this.imageSearchInspector = imageSearchInspector;

        init();
    }

    private void init() {
        imageSortTypeLiveData.setValue(DEFAULT_IMAGE_SORT_TYPE);
        countOfItemInLineLiveData.setValue(DEFAULT_COUNT_OF_ITEM_IN_LINE);
        imageSearchStateLiveData.setValue(ImageSearchState.NONE);
        observeImageSearchApprove();
    }

    @NonNull
    public LiveData<ImageSortType> observeImageSortType() {
        return imageSortTypeLiveData;
    }

    @NonNull
    public LiveData<Integer> observeCountOfItemInLine() {
        return countOfItemInLineLiveData;
    }

    @NonNull
    public LiveData<List<ImageDocument>> observeImageDocumentList() {
        return imageDocumentListLiveData;
    }

    @NonNull
    public LiveData<ImageSearchState> observeImageSearchState() {
        return imageSearchStateLiveData;
    }

    public void changeImageSortType(final ImageSortType imageSortType) {
        imageSortTypeLiveData.setValue(imageSortType);

        final String previousKeyword = imageSearchInspector.getPreviousRequestKeyword();
        if(!TextUtils.isEmpty(previousKeyword)) {
            imageSearchInspector.submitFirstImageSearchRequest(previousKeyword, getImageSortType());
        }
    }

    public void changeCountOfItemInLine(final int countOfItemInLine) {
        countOfItemInLineLiveData.setValue(countOfItemInLine);
    }

    public void loadImageList(@NonNull final String keyword) {
        clearBeforeImageDocumentList();
        imageSearchInspector.submitFirstImageSearchRequest(keyword, getImageSortType());
    }

    public void retryLoadMoreImageList() {
        imageSearchInspector.submitRetryRequest(DEFAULT_IMAGE_SORT_TYPE);
    }

    public void loadMoreImageListIfPossible(final int displayPosition) {
        if(isRemainingMoreData()) {
            final List<ImageDocument> imageDocumentList = imageDocumentListLiveData.getValue();
            if (imageDocumentList != null) {
                imageSearchInspector.submitPreloadRequest(
                    displayPosition,
                    imageDocumentList.size(),
                    getImageSortType(),
                    getCountOfItemInLine()
                );
            }
        }
    }

    private void observeImageSearchApprove() {
        imageSearchInspector.observeImageSearchApprove(this::requestImageSearchToRepository);
    }

    private void requestImageSearchToRepository(final ImageSearchRequest imageSearchRequest) {
        changeImageSearchState(ImageSearchState.NONE);

        registerDisposable(
            imageRepository.requestImageList(imageSearchRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handlingReceivedResponse, this::handlingError)
        );
    }

    private void handlingReceivedResponse(final ImageSearchResult imageSearchResult) {
        changeImageSearchState(ImageSearchState.SUCCESS);

        if(imageSearchResult.getImageSearchRequest().isFirstRequest()) {
            clearBeforeImageDocumentList();
        }
        updateSearchMetaInfo(imageSearchResult.getSearchMetaInfo());
        updateImageInfoList(imageSearchResult.getImageDocumentList());
    }

    private void updateSearchMetaInfo(final SearchMetaInfo searchMetaInfo) {
        this.searchMetaInfo = searchMetaInfo;
    }

    private void updateImageInfoList(final List<ImageDocument> receivedImageDocumentList) {
        final List<ImageDocument> oldList = imageDocumentListLiveData.getValue();

        final List<ImageDocument> newList = new ArrayList<>(oldList == null ? new ArrayList<>() : oldList);
        newList.addAll(receivedImageDocumentList);

        if(CollectionUtil.isEmpty(newList)) {
            updateMessage(R.string.success_image_search_no_result);
        } else if(isNotRemainingMoreData()){
            updateMessage(R.string.success_image_search_last_data);
        }

        imageDocumentListLiveData.setValue(newList);
    }

    private void clearBeforeImageDocumentList() {
        imageDocumentListLiveData.setValue(new ArrayList<>());
    }

    private void handlingError(final Throwable throwable) {
        changeImageSearchState(ImageSearchState.FAIL);

        if(throwable instanceof ImageSearchException) {
            final ImageSearchError error = ((ImageSearchException)throwable).getImageSearchError();
            updateMessage(error.getErrorMessageResourceId());
        } else {
            Log.d(TAG, throwable.getMessage());
        }
    }

    private boolean isNotRemainingMoreData() {
        return !isRemainingMoreData();
    }

    private boolean isRemainingMoreData() {
        return searchMetaInfo == null || !searchMetaInfo.isEnd();
    }

    private void changeImageSearchState(final ImageSearchState imageSearchState) {
        final ImageSearchState previousImageSearchState = imageSearchStateLiveData.getValue();
        if(previousImageSearchState != imageSearchState) {
            imageSearchStateLiveData.setValue(imageSearchState);
        }
    }

    private ImageSortType getImageSortType() {
        ImageSortType imageSortType = imageSortTypeLiveData.getValue();
        return imageSortType != null ? imageSortType : DEFAULT_IMAGE_SORT_TYPE;
    }

    private int getCountOfItemInLine() {
        Integer countOfItemInLine = countOfItemInLineLiveData.getValue();
        return countOfItemInLine != null ? countOfItemInLine : DEFAULT_COUNT_OF_ITEM_IN_LINE;
    }

}
