package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailViewModel extends BaseViewModel {

    @Nullable
    private ImageInfo imageInfo;
    @NonNull
    private final MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();
    @NonNull
    private final SingleLiveEvent<String> docUrlSingleLiveData = new SingleLiveEvent<>();

    public ImageDetailViewModel(@NonNull Application application) {
        super(application);
    }

    @NonNull
    public LiveData<String> observeImageUrl() {
        return imageUrlLiveData;
    }

    @NonNull
    public LiveData<String> observeMoveWebEvent() {
        return docUrlSingleLiveData;
    }

    public void setImageInfo(@Nullable ImageInfo imageInfo) {
        this.imageInfo = imageInfo;
        if(imageInfo != null) {
            imageUrlLiveData.setValue(imageInfo.getImageUrl());
        }
    }

    public void onClickWebButton() {
        if(imageInfo != null) {
            final String docUrl = imageInfo.getDocUrl();
            docUrlSingleLiveData.setValue(docUrl);
        }
    }

}
