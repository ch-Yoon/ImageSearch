package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.ui.common.livedata.SingleLiveEvent;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.DetailImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewModel;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailViewModel extends BaseViewModel {

    private static final String TAG = ImageDetailViewModel.class.getName();

    @NonNull
    private final ImageRepository imageRepository;

    @NonNull
    private final MutableLiveData<String> imageUrlLiveData = new MutableLiveData<>();

    @NonNull
    private final SingleLiveEvent<String> docUrlLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<Void> finishEvent = new SingleLiveEvent<>();

    @Nullable
    private DetailImageInfo detailImageInfo;

    ImageDetailViewModel(@NonNull Application application,
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

    public void loadImage(@Nullable String id) {
        if(id != null) {
            registerDisposable(
                imageRepository.requestImageDetailInfo(id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        this::updateImageDetailInfo,
                        throwable -> {
                            handlingUnknownError();
                            Log.d(TAG, throwable.getMessage());
                        }
                    )
            );
        } else {
            handlingUnknownError();
        }
    }

    public void onClickWebButton() {
        if(detailImageInfo != null) {
            final String docUrl = detailImageInfo.getDocUrl();
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

    private void updateImageDetailInfo(DetailImageInfo detailImageInfo) {
        this.detailImageInfo = detailImageInfo;
        imageUrlLiveData.setValue(detailImageInfo.getImageUrl());
    }

    private void handlingUnknownError() {
        showMessageLiveEvent.setValue(getString(R.string.error_unknown_error));
        finishEvent.call();
    }

}
