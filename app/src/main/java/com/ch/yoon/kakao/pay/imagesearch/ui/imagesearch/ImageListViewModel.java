package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.error.ImageSearchException;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchMeta;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchResponse;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper.ImageSearchInspector;

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
    private final MutableLiveData<List<ImageInfo>> imageInfoListLiveData = new MutableLiveData<>();
    @NonNull
    private final SingleLiveEvent<String> errorMessageLiveData = new SingleLiveEvent<>();

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
        observeImageSearchApprove();
    }

    @NonNull
    public LiveData<Integer> observeCountOfItemInLine() {
        return countOfItemInLineLiveData;
    }

    @NonNull
    public LiveData<List<ImageInfo>> observeImageInfoList() {
        return imageInfoListLiveData;
    }

    @NonNull
    public SingleLiveEvent<String> observeErrorMessage() {
        return errorMessageLiveData;
    }

    public void changeCountOfItemInLine(int countOfItemInLine) {
        countOfItemInLineLiveData.setValue(countOfItemInLine);
    }

    public void loadImageList(@NonNull String keyword) {
        final int countOfItemInLine = getCountOfItemInLine();

        imageSearchInspector.submitFirstImageSearchRequest(
            keyword,
            DEFAULT_IMAGE_SORT_TYPE,
            countOfItemInLine
        );
    }

    public void loadMoreImageListIfPossible(int displayPosition) {
        if(remainingDataOnRepository) {
            final List<ImageInfo> imageInfoList = imageInfoListLiveData.getValue();
            if (imageInfoList != null) {
                final int dataTotalSize = imageInfoList.size();
                final int countOfItemInLine = getCountOfItemInLine();

                imageSearchInspector.submitPreloadRequest(
                    displayPosition,
                    dataTotalSize,
                    DEFAULT_IMAGE_SORT_TYPE,
                    countOfItemInLine
                );
            }
        }
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
        final ImageSearchMeta meta = imageSearchResponse.getMeta();
        updateMetaInfo(meta);

        final List<ImageInfo> imageInfoList = imageSearchResponse.getImageInfoList();
        updateImageInfoList(imageInfoList);
    }

    private void updateMetaInfo(ImageSearchMeta meta) {
        if(meta != null) {
            remainingDataOnRepository = !meta.isEnd();
        } else {
            remainingDataOnRepository = false;
        }
    }

    private void updateImageInfoList(List<ImageInfo> imageInfoList) {
        if(imageInfoList != null) {
            final List<ImageInfo> previousImageInfoList = imageInfoListLiveData.getValue();

            List<ImageInfo> newImageInfoList;
            if(previousImageInfoList == null) {
                newImageInfoList = new ArrayList<>();
            } else {
                newImageInfoList = new ArrayList<>(previousImageInfoList);
            }
            newImageInfoList.addAll(imageInfoList);

            imageInfoListLiveData.setValue(newImageInfoList);
        }
    }

    private void handlingError(Throwable throwable) {
        if(throwable instanceof ImageSearchException) {
            final int messageResourceId = ((ImageSearchException) throwable).getErrorMessageResourceId();
            errorMessageLiveData.setValue(getString(messageResourceId));
        }

        Log.d(TAG, throwable.getMessage());
    }

}
