package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;
    @NonNull
    private final ImageSearchRepository imageSearchRepository;
    @NonNull
    private final ImageSearchInspector imageSearchInspector;

    public ImageListViewModelFactory(@NonNull Application application,
                                     @NonNull ImageSearchRepository imageSearchRepository,
                                     @NonNull ImageSearchInspector imageSearchInspector) {
        this.application = application;
        this.imageSearchRepository = imageSearchRepository;
        this.imageSearchInspector = imageSearchInspector;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImageListViewModel.class)) {
            return (T) new ImageListViewModel(application, imageSearchRepository, imageSearchInspector);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

}