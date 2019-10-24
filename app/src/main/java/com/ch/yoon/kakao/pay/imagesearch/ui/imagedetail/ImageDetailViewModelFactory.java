package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageDetailViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;

    ImageDetailViewModelFactory(@NonNull Application application) {
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImageDetailViewModel.class)) {
            return (T) new ImageDetailViewModel(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}