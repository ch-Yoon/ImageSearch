package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageListBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageDatabase;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail.ImageDetailActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModelFactory;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.ImageListAdapter;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.SearchHistoryAdapter;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModelFactory;

public class ImageListActivity extends BaseActivity<ActivityImageListBinding> {

    private ActivityImageListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = binding(R.layout.activity_image_list);

        initSearchBoxViewModel();
        initImageListModel();

        initHistoryRecyclerView();
        initImageListRecyclerView();
    }

    private void initSearchBoxViewModel() {
        final SearchBoxViewModel viewModel = ViewModelProviders.of(this, new SearchBoxViewModelFactory(
                getApplication(),
                ImageRepositoryImpl.getInstance(
                    ImageLocalDataSource.getInstance(
                        ImageDatabase.getInstance(getApplicationContext()).imageDocumentDao()
                    ),
                    ImageRemoteDataSource.getInstance())
            )
        ).get(SearchBoxViewModel.class);

        viewModel.observeSearchKeywordEvent().observe(this, keyword ->
            binding.getImageListViewModel().loadImageList(keyword)
        );

        viewModel.observeSearchBoxFinishEvent().observe(this, voidEvent ->
            finish()
        );

        viewModel.observeShowMessage().observe(this, this::showToast);

        binding.setSearchBoxViewModel(viewModel);
    }

    private void initHistoryRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.searchHistoryRecyclerView.setLayoutManager(linearLayoutManager);

        SearchHistoryAdapter adapter = new SearchHistoryAdapter();
        adapter.setOnSearchLogClickListener((searchLog, position) ->
            binding.getSearchBoxViewModel().clickSearchButton(searchLog.getKeyword())
        );

        adapter.setOnLogDeleteClickListener((searchLog, position) ->
            binding.getSearchBoxViewModel().clickKeywordDeleteButton(searchLog.getKeyword())
        );

        binding.searchHistoryRecyclerView.setAdapter(adapter);
    }

    private void initImageListModel() {
        final ImageListViewModel viewModel = ViewModelProviders.of(this, new ImageListViewModelFactory(
            getApplication(),
            ImageRepositoryImpl.getInstance(
                ImageLocalDataSource.getInstance(
                    ImageDatabase.getInstance(getApplicationContext()).imageDocumentDao()
                ),
                ImageRemoteDataSource.getInstance()),
            new ImageSearchInspector(1, 50, 80, 20)
        )).get(ImageListViewModel.class);

        viewModel.observeMessage().observe(this, this::showToast);

        binding.setImageListViewModel(viewModel);
    }

    private void initImageListRecyclerView() {
        final ImageListAdapter imageListAdapter = new ImageListAdapter();

        imageListAdapter.setOnBindPositionListener(position ->
            binding.getImageListViewModel().loadMoreImageListIfPossible(position)
        );

        imageListAdapter.setOnListItemClickListener((imageInfo, position) -> {
            Intent imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(this, imageInfo.getId());
            startActivity(imageDetailIntent);
        });

        imageListAdapter.setOnFooterItemClickListener(() ->
            binding.getImageListViewModel().retryLoadMoreImageList()
        );

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

    @Override
    public void onBackPressed() {
        binding.getSearchBoxViewModel().clickBackPress();
    }

}