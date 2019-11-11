package com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageSearchBinding
import com.ch.yoon.kakao.pay.imagesearch.presentation.base.BaseActivity
import com.ch.yoon.kakao.pay.imagesearch.presentation.imagedetail.ImageDetailActivity
import com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch.imagelist.ImageListViewModel
import com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch.imagelist.ImageListAdapter
import com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch.searchbox.SearchBoxViewModel
import kotlinx.android.synthetic.main.activity_image_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class ImageSearchActivity : BaseActivity<ActivityImageSearchBinding>() {

    private val searchBoxViewModel: SearchBoxViewModel by viewModel()
    private val imageListViewModel: ImageListViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_image_search
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSearchBoxViewModel()
        observeSearchBoxViewModel()

        initImageListViewModel()
        observeImageListViewModel()

        initSearchLogRecyclerView()
        initImageRecyclerView()

        searchButton.setOnClickListener {
            binding.suggestionSearchView.show()
        }
    }

    private fun initSearchBoxViewModel() {
        binding.searchBoxViewModel = searchBoxViewModel

        if (isActivityFirstCreate) {
            searchBoxViewModel.loadSearchLogList()
        }
    }

    private fun observeSearchBoxViewModel() {
        val owner = this
        with(searchBoxViewModel) {
            searchEvent.observe(owner, Observer { keyword ->
                imageListViewModel.loadImageList(keyword)
            })

            searchBoxFinishEvent.observe(owner, Observer {
                finish()
            })

            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })
        }
    }

    private fun initSearchLogRecyclerView() {
//        binding.suggestionSearchView.setAdapter(SearchLogAdapter(searchBoxViewModel))
    }

    private fun initImageListViewModel() {
        binding.imageListViewModel = imageListViewModel
    }

    private fun observeImageListViewModel() {
        val owner = this
        with(imageListViewModel) {
            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToDetailScreenEvent.observe(owner, Observer { imageDocument ->
                val imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(owner, imageDocument)
                startActivity(imageDetailIntent)
            })
        }
    }

    private fun initImageRecyclerView() {
        binding.imageRecyclerView.apply {
            adapter = ImageListAdapter(imageListViewModel).apply {
                onBindPosition = { position ->
                    imageListViewModel.loadMoreImageListIfPossible(position)
                }
            }

            layoutManager = (layoutManager as GridLayoutManager).apply {
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

    override fun onBackPressed() {
        searchBoxViewModel.onClickBackPressButton()
    }
}