package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageListBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.adapter.ImageListAdapter;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.helper.ImageSearchInspector;

public class ImageListActivity extends BaseActivity<ActivityImageListBinding> {

    private ActivityImageListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_list);

        initImageSearchViewModel();
        initRecyclerView();
    }

    private void initImageSearchViewModel() {
        final ImageListViewModel viewModel = ViewModelProviders.of(this, new ImageListViewModelFactory(
            getApplication(),
            ImageRepositoryImpl.getInstance(ImageRemoteDataSource.getInstance()),
            new ImageSearchInspector(1, 50, 80, 20, 10)
        )).get(ImageListViewModel.class);

        viewModel.observeErrorMessage().observe(this, this::showToast);

        binding.setImageListViewModel(viewModel);
    }

    private void initRecyclerView() {
        final ImageListAdapter imageListAdapter = new ImageListAdapter();
        imageListAdapter.setOnBindPositionListener(position ->
            binding.getImageListViewModel().loadMoreImageListIfPossible(position)
        );

        binding.imageRecyclerView.setAdapter(imageListAdapter);
    }

}