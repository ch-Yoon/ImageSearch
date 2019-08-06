package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.ViewModelProviders;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageDetailBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageDatabase;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.dao.ImageSearchDao;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;

import static com.google.gson.reflect.TypeToken.get;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    public static final String EXTRA_IMAGE_UNIQUE_INFO_KEY = "EXTRA_IMAGE_UNIQUE_INFO_KEY";

    private ActivityImageDetailBinding binding;

    public static Intent getImageDetailActivityIntent(@NonNull Context context,
                                                      @Nullable String id) {
        Intent imageDetailIntent = new Intent(context, ImageDetailActivity.class);
        imageDetailIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE_UNIQUE_INFO_KEY, id);
        return imageDetailIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_detail);

        String uniqueImageInfo = getIntent().getStringExtra(EXTRA_IMAGE_UNIQUE_INFO_KEY);
        checkPassedInfo(uniqueImageInfo);

        initBackArrow();
        initImageDetailViewModel(uniqueImageInfo);
        observeImageDetailViewModel();
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

    private void checkPassedInfo(String uniqueImageInfo) {
        if(uniqueImageInfo == null) {
            showToast(R.string.error_unknown_error);
            finish();
        }
    }

    private void initBackArrow() {
        setSupportActionBar(binding.mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initImageDetailViewModel(String uniqueImageInfo) {
        final ImageSearchDao imageSearchDao = ImageDatabase.getInstance(getApplicationContext()).imageDocumentDao();
        final ImageLocalDataSource localDataSource = ImageLocalDataSource.getInstance(imageSearchDao);
        final ImageRemoteDataSource remoteDataSource = ImageRemoteDataSource.getInstance();
        final ImageRepository repository = ImageRepositoryImpl.getInstance(localDataSource, remoteDataSource);

        final ImageDetailViewModel viewModel = ViewModelProviders.of(this, new ImageDetailViewModelFactory(
            getApplication(), repository
        )).get(ImageDetailViewModel.class);

        viewModel.loadImage(uniqueImageInfo);

        binding.setImageDetailViewModel(viewModel);
    }

    private void observeImageDetailViewModel() {
        binding.getImageDetailViewModel()
            .observeErrorMessage()
            .observe(this, this::showToast);

        binding.getImageDetailViewModel()
            .observeMoveWebEvent()
            .observe(this, url -> {
                Uri webUri = Uri.parse(url);
                Intent movieWebIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(movieWebIntent);
            });
    }

}
