package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDetailInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailViewModel extends BaseViewModel {

    @NonNull
    private final ImageRepository imageRepository;

    @NonNull
    private final MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();
    @NonNull
    private final SingleLiveEvent<String> docUrlSingleLiveData = new SingleLiveEvent<>();

    @Nullable
    private ImageDetailInfo imageDetailInfo;

    public ImageDetailViewModel(@NonNull Application application,
                                @NonNull ImageRepository imageRepository) {
        super(application);
        this.imageRepository = imageRepository;
    }

    @NonNull
    public LiveData<String> observeImageUrl() {
        return imageUrlLiveData;
    }

    @NonNull
    public LiveData<String> observeMoveWebEvent() {
        return docUrlSingleLiveData;
    }

    public void loadImage(@NonNull String uniqueImageInfo) {
        registerDisposable(
            imageRepository.requestImageDetailInfo(uniqueImageInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateImageDetailInfo, throwable -> {})
        );
    }

    private void updateImageDetailInfo(ImageDetailInfo imageDetailInfo) {
        this.imageDetailInfo = imageDetailInfo;
        imageUrlLiveData.setValue(imageDetailInfo.getImageUrl());
    }

    public void onClickWebButton() {
        if(imageDetailInfo != null) {
            final String docUrl = imageDetailInfo.getDocUrl();
            docUrlSingleLiveData.setValue(docUrl);
        }
    }

}
