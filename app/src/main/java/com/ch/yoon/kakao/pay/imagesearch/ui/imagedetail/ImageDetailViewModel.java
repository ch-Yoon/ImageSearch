package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.Document;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailViewModel extends BaseViewModel {

    @Nullable
    private Document document;
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

    public void setDocument(@Nullable Document document) {
        this.document = document;
        if(document != null) {
            imageUrlLiveData.setValue(document.getImageUrl());
        }
    }

    public void onClickWebButton() {
        if(document != null) {
            final String docUrl = document.getDocUrl();
            docUrlSingleLiveData.setValue(docUrl);
        }
    }

}
