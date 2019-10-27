package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageDatabase
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.ImageLocalDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.ImageRemoteDataSource
import com.ch.yoon.kakao.pay.imagesearch.data.repository.ImageRepositoryImpl
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageSearchBinding
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity
import com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail.ImageDetailActivity
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.ImageListViewModelFactory
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.ImageListAdapter
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.helper.ImageSearchInspector
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModel
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchBoxViewModelFactory
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.SearchLogAdapter

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class ImageSearchActivity : BaseActivity<ActivityImageSearchBinding>() {

    private lateinit var searchBoxViewModel: SearchBoxViewModel
    private lateinit var imageListViewModel: ImageListViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_image_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchBoxViewModel()
        initSearchKeywordEditText()
        observeSearchBoxViewModel()

        initImageListViewModel()
        observeImageListViewModel()

        initSearchLogRecyclerView()
        initImageRecyclerView()
    }

    private fun initSearchBoxViewModel() {
        val searchLogDao = ImageDatabase.getInstance(applicationContext).searchLogDao()
        val localDataSource = ImageLocalDataSource.getInstance(searchLogDao)
        val remoteDataSource = ImageRemoteDataSource.getInstance()
        val repository = ImageRepositoryImpl.getInstance(localDataSource, remoteDataSource)

        searchBoxViewModel = ViewModelProviders.of(this,
            SearchBoxViewModelFactory(application, repository)
        ).get(SearchBoxViewModel::class.java)

        if (isActivityFirstCreate) {
            searchBoxViewModel.loadSearchLogList()
        }

        binding.searchBoxViewModel = searchBoxViewModel
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initSearchKeywordEditText() {
        binding.keywordEditText.setOnEditorActionListener{ v, actionId, _ ->
            if (actionId == KeyEvent.KEYCODE_ENDCALL) {
                binding.searchBoxViewModel?.onClickSearchButton(v.text.toString())
                true
            } else {
                false
            }
        }

        binding.keywordEditText.setOnTouchListener { _, event ->
            if (MotionEvent.ACTION_UP == event.action) {
                binding.searchBoxViewModel?.onClickSearchBox()
            }
            false
        }
    }

    private fun observeSearchBoxViewModel() {
        searchBoxViewModel.searchKeyword.observe(this, Observer { keyword ->
            binding.imageListViewModel?.loadImageList(keyword)
        })

        searchBoxViewModel.searchBoxFinishEvent.observe(this, Observer { finish() })

        searchBoxViewModel.showMessageEvent.observe(this, Observer { message ->
            showToast(message)
        })
    }

    private fun initSearchLogRecyclerView() {
        binding.searchLogRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ImageSearchActivity, RecyclerView.VERTICAL, false)
            adapter = SearchLogAdapter().apply {
                setOnSearchLogClickListener { searchLog, _ ->
                    binding.searchBoxViewModel?.onClickSearchButton(searchLog.keyword)
                }
                setOnLogDeleteClickListener { searchLog, _ ->
                    binding.searchBoxViewModel?.onClickSearchLogDeleteButton(searchLog)
                }
            }
        }
    }

    private fun initImageListViewModel() {
        val searchLogDao = ImageDatabase.getInstance(applicationContext).searchLogDao()
        val localDataSource = ImageLocalDataSource.getInstance(searchLogDao)
        val remoteDataSource = ImageRemoteDataSource.getInstance()
        val repository = ImageRepositoryImpl.getInstance(localDataSource, remoteDataSource)
        val imageSearchInspector = ImageSearchInspector(1, 50, 80, 20)

        imageListViewModel = ViewModelProviders.of(this, ImageListViewModelFactory(
            application, repository, imageSearchInspector
        )).get(ImageListViewModel::class.java)

        binding.imageListViewModel = imageListViewModel
    }

    private fun observeImageListViewModel() {
        imageListViewModel.observeShowMessage().observe(this, Observer { message ->
            showToast(message)
        })
    }

    private fun initImageRecyclerView() {
        binding.imageRecyclerView.apply {
            adapter = ImageListAdapter().apply {
                setOnBindPositionListener { position ->
                    binding.imageListViewModel?.loadMoreImageListIfPossible(position)
                }
                setOnFooterItemClickListener {
                    binding.imageListViewModel?.retryLoadMoreImageList()
                }
                setOnListItemClickListener { imageDocument, _ ->
                    val imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(this@ImageSearchActivity, imageDocument)
                    startActivity(imageDetailIntent)
                }
            }

            layoutManager = layoutManager?.apply {
                if (this is GridLayoutManager) {
                    spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                        override fun getSpanSize(position: Int): Int {
                            return if ((adapter as ImageListAdapter).isFooterViewPosition(position)) {
                                spanCount
                            } else {
                                1
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        binding.searchBoxViewModel?.onClickBackPressButton()
    }

}