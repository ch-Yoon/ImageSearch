package com.ch.yoon.imagesearch.presentation.imagesearch

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.databinding.ActivityImageSearchBinding
import com.ch.yoon.imagesearch.presentation.base.BaseActivity
import com.ch.yoon.imagesearch.presentation.favorite.FavoriteActivity
import com.ch.yoon.imagesearch.presentation.imagedetail.ImageDetailActivity
import com.ch.yoon.imagesearch.presentation.imagesearch.imagelist.ImageListViewModel
import com.ch.yoon.imagesearch.presentation.imagesearch.imagelist.ImageListAdapter
import com.ch.yoon.imagesearch.presentation.imagesearch.searchbox.SearchBoxViewModel
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

        initActionBar()

        initSearchBoxViewModel()
        observeSearchBoxViewModel()

        initImageListViewModel()
        observeImageListViewModel()
        initImageRecyclerView()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.image_search_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let { menuItem ->
            when(menuItem.itemId) {
                R.id.action_search -> {
                    searchBoxViewModel.onClickShowButton()
                }
                R.id.action_favorite -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        searchBoxViewModel.onClickBackPressButton()
    }
}