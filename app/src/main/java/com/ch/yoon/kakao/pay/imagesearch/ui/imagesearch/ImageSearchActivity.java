package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageSearchBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;

public class ImageSearchActivity extends BaseActivity<ActivityImageSearchBinding> {

    private ActivityImageSearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_search);

        initViewModel();
    }

    private void initViewModel() {
        ImageDataSource remoteDataSource = ImageRemoteDataSource.getInstance();
        ImageRepository imageRepository = ImageRepositoryImpl.getInstance(remoteDataSource);
        ImageListViewModelFactory factory = new ImageListViewModelFactory(imageRepository);
        binding.setSearchViewModel(ViewModelProviders.of(this, factory).get(ImageListViewModel.class));
    }

}
