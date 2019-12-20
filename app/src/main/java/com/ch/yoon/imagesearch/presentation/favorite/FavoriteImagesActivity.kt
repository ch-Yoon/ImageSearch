package com.ch.yoon.imagesearch.presentation.favorite

import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.databinding.ActivityFavoriteBinding
import com.ch.yoon.imagesearch.extension.throttleFirstWithHalfSecond
import com.ch.yoon.imagesearch.presentation.base.RxBaseActivity
import com.ch.yoon.imagesearch.presentation.detail.ImageDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteImagesActivity : RxBaseActivity<ActivityFavoriteBinding>() {

    private val favoriteImagesViewModel: FavoriteImagesViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_favorite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()

        initFavoriteListViewModel()
        initFavoriteRecyclerView()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFavoriteListViewModel() {
        binding.favoriteListViewModel = favoriteImagesViewModel

        val owner = this
        with(favoriteImagesViewModel) {
            if(isActivityFirstCreate) {
                favoriteImagesViewModel.loadFavoriteImageList()
                favoriteImagesViewModel.observeChangingFavoriteImage()
            }

            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToDetailScreenEvent.observe(owner, Observer { imageDocument ->
                val imageDetailIntent = ImageDetailActivity.getImageDetailActivityIntent(owner, imageDocument)
                startActivity(imageDetailIntent)
            })
        }
    }

    private fun initFavoriteRecyclerView() {
        binding.favoriteRecyclerView.apply {
            adapter = FavoriteImagesAdapter().apply {
                itemClicks.throttleFirstWithHalfSecond()
                    .subscribe { imageDocument -> favoriteImagesViewModel.onClickImage(imageDocument) }
                    .disposeByOnDestroy()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                false
            }
        }
    }
}
