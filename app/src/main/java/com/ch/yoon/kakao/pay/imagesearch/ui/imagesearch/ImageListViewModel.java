package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.ImageSearchResult;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.model.ImageSortType;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.request.ImageListRequest;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageSearchMeta;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-01.
 */
public class ImageListViewModel extends BaseViewModel {

    @NonNull
    private final ImageRepository imageRepository;

    private boolean remainingMoreData;
    private final MutableLiveData<List<String>> imageUrls = new MutableLiveData<>();

    public ImageListViewModel(@NonNull ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @NonNull
    public LiveData<List<String>> observeImageUrls() {
        return imageUrls;
    }

    public void requestImageList(@NonNull String keyword) {
        registerDisposable(
            imageRepository.requestImageList(new ImageListRequest(keyword, ImageSortType.ACCURACY, 1, 80))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateImageList, throwable -> { })
        );
    }

    private void updateImageList(ImageSearchResult imageSearchResult) {
        remainingMoreData = imageSearchResult.isEnd();
        imageUrls.setValue(imageSearchResult.getImageUrls());
    }

}
