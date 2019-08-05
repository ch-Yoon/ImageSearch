package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageDetailBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.ImageListViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.ImageListViewModelFactory;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper.ImageSearchInspector;
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    public static final String EXTRA_IMAGE_DETAIL_INFO_KEY = "EXTRA_IMAGE_DETAIL_INFO_KEY";

    private ActivityImageDetailBinding binding;

    public static Intent getImageDetailActivityIntent(@NonNull Context context,
                                                      @NonNull ImageInfo imageInfo) {
        Intent imageDetailIntent = new Intent(context, ImageDetailActivity.class);
        imageDetailIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE_DETAIL_INFO_KEY, imageInfo);
        return imageDetailIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_detail);

        initViewModel();
    }

    private void initViewModel() {
        final ImageDetailViewModel viewModel = ViewModelProviders.of(this, new ImageDetailViewModelFactory(
            getApplication()
        )).get(ImageDetailViewModel.class);

        binding.setImageDetailViewModel(viewModel);
    }

    private void handlingReceivedIntent() {
        ImageInfo imageInfo = getIntent().getParcelableExtra(EXTRA_IMAGE_DETAIL_INFO_KEY);
    }

}
