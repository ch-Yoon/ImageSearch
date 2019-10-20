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
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;

import static com.google.gson.reflect.TypeToken.get;

public class ImageDetailActivity extends BaseActivity<ActivityImageDetailBinding> {

    public static final String EXTRA_IMAGE_DOCUMENT_KEY = "EXTRA_IMAGE_DOCUMENT_KEY";

    private ActivityImageDetailBinding binding;

    public static Intent getImageDetailActivityIntent(@NonNull Context context,
                                                      @NonNull ImageDocument imageDocument) {
        Intent imageDetailIntent = new Intent(context, ImageDetailActivity.class);
        imageDetailIntent.putExtra(ImageDetailActivity.EXTRA_IMAGE_DOCUMENT_KEY, imageDocument);
        return imageDetailIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_detail);

        ImageDocument imageDocument = getIntent().getParcelableExtra(EXTRA_IMAGE_DOCUMENT_KEY);

        initBackArrow();
        initImageDetailViewModel(imageDocument);
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

    private void initBackArrow() {
        setSupportActionBar(binding.mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initImageDetailViewModel(ImageDocument imageDocument) {
        final ImageDetailViewModel viewModel = ViewModelProviders.of(this, new ImageDetailViewModelFactory(
            getApplication()
        )).get(ImageDetailViewModel.class);

        viewModel.showImageDetailInfo(imageDocument);

        binding.setImageDetailViewModel(viewModel);
    }

    private void observeImageDetailViewModel() {
        binding.getImageDetailViewModel()
            .observeShowMessage()
            .observe(this, this::showToast);

        binding.getImageDetailViewModel()
            .observeMoveWebEvent()
            .observe(this, url -> {
                Uri webUri = Uri.parse(url);
                Intent movieWebIntent = new Intent(Intent.ACTION_VIEW, webUri);
                startActivity(movieWebIntent);
            });

        binding.getImageDetailViewModel()
            .observeFinishEvent()
            .observe(this, aVoid -> finish());
    }

    @Override
    public void onBackPressed() {
        binding.getImageDetailViewModel().onClickBackPress();
    }

}
