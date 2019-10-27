package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageSearchBinding;
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepository;
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepositoryImpl;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageDatabase;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageLocalDataSource;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.dao.SearchLogDao;
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.ImageRemoteDataSource;
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail.ImageDetailActivity;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModelFactory;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.ImageListAdapter;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModel;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.SearchLogAdapter;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModelFactory;

public class ImageSearchActivity extends BaseActivity<ActivityImageSearchBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_image_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSearchBoxViewModel();
        initSearchKeywordEditText();
        observeSearchBoxViewModel();

        initImageListViewModel();
        observeImageListViewModel();

        initSearchLogRecyclerView();
        initImageRecyclerView();
    }

    private void initSearchBoxViewModel() {
        final SearchLogDao searchLogDao = ImageDatabase.getInstance(getApplicationContext()).searchLogDao();
        final ImageLocalDataSource localDataSource = ImageLocalDataSource.getInstance(searchLogDao);
        final ImageRemoteDataSource remoteDataSource = ImageRemoteDataSource.getInstance();
        final ImageRepository repository = ImageRepositoryImpl.getInstance(localDataSource, remoteDataSource);

        final SearchBoxViewModel viewModel = ViewModelProviders.of(this,
            new SearchBoxViewModelFactory(getApplication(), repository)
        ).get(SearchBoxViewModel.class);

        getBinding().setSearchBoxViewModel(viewModel);

        if(isActivityFirstCreate()) {
            viewModel.loadSearchLogList();
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initSearchKeywordEditText() {
        getBinding().keywordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == KeyEvent.KEYCODE_ENDCALL) {
                final String text = v.getText().toString();
                getBinding().getSearchBoxViewModel().onClickSearchButton(text);
                return true;
            }

            return false;
        });

        getBinding().keywordEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                getBinding().getSearchBoxViewModel().onClickSearchBox();
            }
            return false;
        });
    }

    private void observeSearchBoxViewModel() {
        getBinding().getSearchBoxViewModel().getSearchKeyword()
            .observe(this, keyword -> {
                getBinding().getImageListViewModel().loadImageList(keyword);
                getBinding().keywordEditText.setText(keyword);
            });

        getBinding().getSearchBoxViewModel().getSearchBoxFinishEvent()
            .observe(this, voidEvent ->
                finish()
            );

        getBinding().getSearchBoxViewModel().getShowMessageEvent()
            .observe(this, this::showToast);

    }

    private void initSearchLogRecyclerView() {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        getBinding().searchLogRecyclerView.setLayoutManager(linearLayoutManager);

        final SearchLogAdapter adapter = new SearchLogAdapter();
        adapter.setOnSearchLogClickListener((searchLog, position) ->
            getBinding().getSearchBoxViewModel().onClickSearchButton(searchLog.getKeyword())
        );

        adapter.setOnLogDeleteClickListener((searchLog, position) ->
            getBinding().getSearchBoxViewModel().onClickSearchLogDeleteButton(searchLog)
        );

        getBinding().searchLogRecyclerView.setAdapter(adapter);
    }

    private void initImageListViewModel() {
        final SearchLogDao searchLogDao = ImageDatabase.getInstance(getApplicationContext()).searchLogDao();
        final ImageLocalDataSource localDataSource = ImageLocalDataSource.getInstance(searchLogDao);
        final ImageRemoteDataSource remoteDataSource = ImageRemoteDataSource.getInstance();
        final ImageRepository repository = ImageRepositoryImpl.getInstance(localDataSource, remoteDataSource);

        final ImageSearchInspector imageSearchInspector = new ImageSearchInspector(1, 50, 80, 20);

        final ImageListViewModel viewModel = ViewModelProviders.of(this, new ImageListViewModelFactory(
            getApplication(), repository, imageSearchInspector
        )).get(ImageListViewModel.class);

        getBinding().setImageListViewModel(viewModel);
    }

    private void observeImageListViewModel() {
        getBinding().getImageListViewModel()
            .observeShowMessage()
            .observe(this, this::showToast);
    }

    private void initImageRecyclerView() {
        final ImageListAdapter imageListAdapter = new ImageListAdapter();

        imageListAdapter.setOnBindPositionListener(position ->
            getBinding().getImageListViewModel().loadMoreImageListIfPossible(position)
        );

        imageListAdapter.setOnFooterItemClickListener(() ->
            getBinding().getImageListViewModel().retryLoadMoreImageList()
        );

        imageListAdapter.setOnListItemClickListener((imageDocument, position) -> {
            Intent imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(this, imageDocument);
            startActivity(imageDetailIntent);
        });

        GridLayoutManager gridLayoutManager = (GridLayoutManager)getBinding().imageRecyclerView.getLayoutManager();
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

        getBinding().imageRecyclerView.setAdapter(imageListAdapter);
        getBinding().imageRecyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onBackPressed() {
        getBinding().getSearchBoxViewModel().onClickBackPressButton();
    }

}
