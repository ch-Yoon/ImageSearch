package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSortType;
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

    private static final ImageSortType DEFAULT_IMAGE_SORT_TYPE = ImageSortType.ACCURACY;
    private static int DEFAULT_COUNT_OF_ITEM_IN_LINE = 3;

    @NonNull
    private final ImageRepository imageRepository;
    @NonNull
    private final ImageSearchInspector imageSearchInspector = new ImageSearchInspector();

    @NonNull
    private final MutableLiveData<Integer> countOfItemInLineLiveData = new MutableLiveData<>();
    @NonNull
    private final MutableLiveData<List<ImageInfo>> imageInfoListLiveData = new MutableLiveData<>();

    private boolean remainingDataOnRepository = true;

    public ImageListViewModel(@NonNull ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
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

    public void requestImageList(@NonNull String keyword) {
        imageInfoListLiveData.setValue(null);
        imageSearchInspector.submitFirstImageSearchRequest(
            keyword,
            DEFAULT_IMAGE_SORT_TYPE, DEFAULT_COUNT_OF_ITEM_IN_LINE
        );
    }

    public void requestMoreImageIfPossible(int displayPosition) {
        if(remainingDataOnRepository) {
            List<ImageInfo> imageInfoList = imageInfoListLiveData.getValue();
            if (imageInfoList != null) {
                int dataTotalSize = imageInfoList.size();

                imageSearchInspector.submitPreloadRequest(
                    displayPosition,
                    dataTotalSize,
                    DEFAULT_COUNT_OF_ITEM_IN_LINE,
                    DEFAULT_IMAGE_SORT_TYPE
                );
            }
        }
    }

    private void observeImageSearchApprove() {
        imageSearchInspector.observeImageSearchApprove(this::requestImageSearchToRepository);
    }

    private void requestImageSearchToRepository(ImageSearchRequest imageSearchRequest) {
        registerDisposable(
            imageRepository.requestImageList(imageSearchRequest)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::applyReceivedResponse, throwable -> { })
        );
    }

    private void applyReceivedResponse(ImageSearchResponse imageSearchResponse) {
        ImageSearchMeta meta = imageSearchResponse.getMeta();
        updateMetaInfo(meta);

        List<ImageInfo> imageInfoList = imageSearchResponse.getImageInfoList();
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
            List<ImageInfo> newImageInfoList;
            List<ImageInfo> previousImageInfoList = imageInfoListLiveData.getValue();

            if(previousImageInfoList == null) {
                newImageInfoList = new ArrayList<>();
            } else {
                newImageInfoList = new ArrayList<>(previousImageInfoList);
            }

            newImageInfoList.addAll(imageInfoList);

            imageInfoListLiveData.setValue(newImageInfoList);
        }
    }

}
