package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel2;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailViewModel extends BaseViewModel2 {

    private static final String TAG = ImageDetailViewModel.class.getName();

    @NonNull
    private final MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<String> docUrlLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> finishEvent = new SingleLiveEvent<>();

    @Nullable
    private ImageDocument imageDocument;

    ImageDetailViewModel(@NonNull Application application) {
        super(application);
    }

    @NonNull
    public LiveData<String> observeImageUrl() {
        return imageUrlLiveData;
    }

    @NonNull
    public LiveData<String> observeMoveWebEvent() {
        return docUrlLiveEvent;
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    @NonNull
    public LiveData<Void> observeFinishEvent() {
        return finishEvent;
    }

    public void showImageDetailInfo(@Nullable final ImageDocument imageDocument) {
        if(imageDocument != null) {
            this.imageDocument = imageDocument;
            imageUrlLiveData.setValue(imageDocument.getImageUrl());
        } else {
            handlingUnknownError();
        }
    }

    public void onClickWebButton() {
        if(imageDocument != null) {
            final String docUrl = imageDocument.getDocUrl();
            if(docUrl != null) {
                docUrlLiveEvent.setValue(docUrl);
            } else {
                showMessageLiveEvent.setValue(getString(R.string.error_nonexistent_url));
            }
        } else {
            handlingUnknownError();
        }
    }

    public void onClickBackPress() {
        finishEvent.call();
    }

    private void handlingUnknownError() {
        showMessageLiveEvent.setValue(getString(R.string.error_unknown_error));
        finishEvent.call();

        Log.d(TAG, "image document is null");
    }

}
