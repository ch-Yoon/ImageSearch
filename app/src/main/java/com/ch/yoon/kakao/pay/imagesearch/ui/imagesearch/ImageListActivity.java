package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

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

        initViewModel();
        initRecyclerView();
    }

    private void initViewModel() {
        final ImageListViewModel viewModel = ViewModelProviders.of(this, new ImageListViewModelFactory(
            getApplication(),
            ImageRepositoryImpl.getInstance(ImageRemoteDataSource.getInstance()),
            new ImageSearchInspector(1, 50, 80, 20)
        )).get(ImageListViewModel.class);

        viewModel.observeMessage().observe(this, this::showToast);

        binding.setImageListViewModel(viewModel);
    }

    private void initRecyclerView() {
        final ImageListAdapter imageListAdapter = new ImageListAdapter();

        imageListAdapter.setOnBindPositionListener(position ->
            binding.getImageListViewModel().loadMoreImageListIfPossible(position)
        );

        imageListAdapter.setOnListItemClickListener(position -> {
        });

        imageListAdapter.setOnFooterItemClickListener(() -> {
            binding.getImageListViewModel().retryLoadMoreImageList();
        });

        GridLayoutManager gridLayoutManager = (GridLayoutManager)binding.imageRecyclerView.getLayoutManager();
        if(gridLayoutManager != null) {
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if(imageListAdapter.isFooterViewPosition(position)) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }

        binding.imageRecyclerView.setLayoutManager(gridLayoutManager);
        binding.imageRecyclerView.setAdapter(imageListAdapter);
    }

}