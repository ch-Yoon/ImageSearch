package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.extentions.SingleLiveEvent;
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
    private final SingleLiveEvent<String> docUrlSingleLiveEvent = new SingleLiveEvent<>();
    @NonNull
    private final SingleLiveEvent<String> showMessageLiveEvent = new SingleLiveEvent<>();

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
        return docUrlSingleLiveEvent;
    }

    @NonNull
    public LiveData<String> observeShowMessage() {
        return showMessageLiveEvent;
    }

    public void loadImage(@NonNull String uniqueImageInfo) {
        registerDisposable(
            imageRepository.requestImageDetailInfo(uniqueImageInfo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    this::updateImageDetailInfo,
                    throwable -> Log.d(TAG, throwable.getMessage())
                )
        );
    }

    private void updateImageDetailInfo(DetailImageInfo detailImageInfo) {
        this.detailImageInfo = detailImageInfo;
        imageUrlLiveData.setValue(detailImageInfo.getImageUrl());
    }

    public void onClickWebButton() {
        if(detailImageInfo != null) {
            final String docUrl = detailImageInfo.getDocUrl();
            docUrlSingleLiveEvent.setValue(docUrl);
        } else {
            docUrlSingleLiveEvent.setValue(getString(R.string.error_unknown_error));
        }
    }

}
