package com.ch.yoon.imagesearch.presentation.search

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.remote.kakao.request.ImageSortType
import com.ch.yoon.imagesearch.databinding.ActivityImageSearchBinding
import com.ch.yoon.imagesearch.extension.throttleFirstWithHalfSecond
import com.ch.yoon.imagesearch.presentation.base.RxBaseActivity
import com.ch.yoon.imagesearch.presentation.favorite.FavoriteImagesActivity
import com.ch.yoon.imagesearch.presentation.detail.ImageDetailActivity
import com.ch.yoon.imagesearch.presentation.search.backpress.BackPressViewModel
import com.ch.yoon.imagesearch.presentation.search.imagesearch.ImageSearchViewModel
import com.ch.yoon.imagesearch.presentation.search.imagesearch.ImageSearchResultsAdapter
import com.ch.yoon.imagesearch.presentation.search.searchbox.SearchBoxViewModel
import com.ch.yoon.imagesearch.presentation.search.searchbox.SearchLogsAdapter
import com.ch.yoon.suggetionsearchview.extention.searchButtonClicks
import com.jakewharton.rxbinding2.view.clicks
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class ImageSearchActivity : RxBaseActivity<ActivityImageSearchBinding>() {

    private val backPressViewModel: BackPressViewModel by viewModel()
    private val searchBoxViewModel: SearchBoxViewModel by viewModel()
    private val imageSearchViewModel: ImageSearchViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_image_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()

        initBackPressViewModel()
        initSearchBoxViewModel()
        initImageListViewModel()

        initImageRecyclerView()
        initSuggestionSearchView()
        initSortOptionView()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initBackPressViewModel() {
        val owner = this
        with(backPressViewModel) {
            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            finishEvent.observe(owner, Observer {
                finish()
            })
        }
    }

    private fun initSearchBoxViewModel() {
        val owner = this
        with(searchBoxViewModel) {
            if(isActivityFirstCreate) {
                searchBoxViewModel.loadSearchLogs()
            }

            searchEvent.observe(owner, Observer { keyword ->
                imageSearchViewModel.loadImages(keyword)
            })

            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })
        }

        binding.searchBoxViewModel = searchBoxViewModel
    }

    private fun initImageListViewModel() {
        val owner = this
        with(imageSearchViewModel) {
            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToDetailScreenEvent.observe(owner, Observer { imageDocument ->
                val imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(owner, imageDocument)
                startActivity(imageDetailIntent)
            })
        }

        binding.searchListViewModel = imageSearchViewModel
    }

    private fun initImageRecyclerView() {
        binding.imageRecyclerView.apply {

            adapter = ImageSearchResultsAdapter().apply {
                onBindPosition = { position ->
                    imageSearchViewModel.loadMoreImagesIfPossible(position)
                }

                itemClicks.throttleFirstWithHalfSecond()
                    .subscribe { document -> imageSearchViewModel.onClickImage(document) }
                    .disposeByOnDestroy()

                footerClicks.throttleFirstWithHalfSecond()
                    .subscribe { imageSearchViewModel.retryLoadMoreImageList() }
                    .disposeByOnDestroy()
            }

            layoutManager = (layoutManager as GridLayoutManager).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if ((adapter as ImageSearchResultsAdapter).isFooterViewPosition(position)) {
                            spanCount
                        } else {
                            1
                        }
                    }
                }
            }
        }
    }

    private fun initSuggestionSearchView() {
        val searchLogsAdapter = SearchLogsAdapter().apply {
            itemClicks.throttleFirstWithHalfSecond()
                .subscribe { searchLog -> searchBoxViewModel.onClickSearchButton(searchLog.keyword) }
                .disposeByOnDestroy()

            itemDeleteClicks.throttleFirstWithHalfSecond()
                .subscribe { searchLog -> searchBoxViewModel.onClickSearchLogDeleteButton(searchLog) }
                .disposeByOnDestroy()

            footerClicks.throttleFirstWithHalfSecond()
                .subscribe { searchBoxViewModel.onClickSearchLogAllDelete() }
                .disposeByOnDestroy()
        }

        binding.suggestionSearchView.apply {
            searchButtonClicks().throttleFirstWithHalfSecond()
                .subscribe { keyword -> searchBoxViewModel.onClickSearchButton(keyword) }
                .disposeByOnDestroy()

            setAdapter(searchLogsAdapter)
        }
    }

    private fun initSortOptionView() {
        binding.accuracySortTextView.clicks()
            .throttleFirstWithHalfSecond()
            .subscribe { imageSearchViewModel.changeImageSortType(ImageSortType.ACCURACY) }
            .disposeByOnDestroy()

        binding.recencySortTextView.clicks()
            .throttleFirstWithHalfSecond()
            .subscribe { imageSearchViewModel.changeImageSortType(ImageSortType.RECENCY) }
            .disposeByOnDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.image_search_menu, menu)
        menu?.let {
            it.findItem(R.id.action_search).clicks()
                .subscribe { searchBoxViewModel.onClickShowButton() }
                .disposeByOnDestroy()

            it.findItem(R.id.action_favorite).clicks()
                .throttleFirstWithHalfSecond()
                .subscribe { startActivity(Intent(this, FavoriteImagesActivity::class.java)) }
                .disposeByOnDestroy()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.let { menuItem ->
            when(menuItem.itemId) {
                R.id.action_search -> { true }
                R.id.action_favorite -> { true }
                else -> { false }
            }
        } ?: super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if(searchBoxViewModel.isOpen) {
            searchBoxViewModel.onClickHideButton()
        } else {
            backPressViewModel.onBackPress()
        }
    }
}