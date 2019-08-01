package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListViewModelFactory implements ViewModelProvider.Factory {

    private final ImageRepository imageRepository;

    ImageListViewModelFactory(@NonNull final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImageListViewModel.class)) {
            return (T) new ImageListViewModel(imageRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}