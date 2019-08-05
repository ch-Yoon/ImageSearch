package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageDetailBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;

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

        initBackArrow();
        initViewModel();
    }

    private void initBackArrow() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initViewModel() {
        final ImageDetailViewModel viewModel = ViewModelProviders.of(this, new ImageDetailViewModelFactory(
            getApplication()
        )).get(ImageDetailViewModel.class);

        ImageInfo imageInfo = getIntent().getParcelableExtra(EXTRA_IMAGE_DETAIL_INFO_KEY);
        viewModel.setImageInfo(imageInfo);

        viewModel.observeMoveWebEvent().observe(this, url -> {
            Uri movieWebUri = Uri.parse(url);
            Intent movieWebIntent = new Intent(Intent.ACTION_VIEW, movieWebUri);
            startActivity(movieWebIntent);
        });

        binding.setImageDetailViewModel(viewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
