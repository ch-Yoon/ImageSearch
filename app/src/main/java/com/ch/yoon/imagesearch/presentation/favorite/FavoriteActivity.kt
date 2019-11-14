package com.ch.yoon.imagesearch.presentation.favorite

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.databinding.ActivityFavoriteBinding
import com.ch.yoon.imagesearch.presentation.base.BaseActivity
import com.ch.yoon.imagesearch.presentation.imagedetail.ImageDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>() {

    private val favoriteListViewModel: FavoriteListViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_favorite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()

        initFavoriteListViewModel()
        observeFavoriteListViewModel()
        initFavoriteRecyclerView()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFavoriteListViewModel() {
        binding.favoriteListViewModel = favoriteListViewModel

        if(isActivityFirstCreate) {
            favoriteListViewModel.loadFavoriteImageList()
            favoriteListViewModel.observeChangingFavoriteImage()
        }
    }

    private fun observeFavoriteListViewModel() {
        val owner = this
        with(favoriteListViewModel) {
            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToDetailScreenEvent.observe(owner, Observer { imageDocument ->
                val imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(owner, imageDocument)
                startActivity(imageDetailIntent)
            })

            finishEvent.observe(owner, Observer {
                finish()
            })
        }
    }

    private fun initFavoriteRecyclerView() {
        binding.favoriteRecyclerView.adapter = FavoriteListAdapter(favoriteListViewModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        return if (itemId == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        favoriteListViewModel.onClickBackPress()
    }
}
