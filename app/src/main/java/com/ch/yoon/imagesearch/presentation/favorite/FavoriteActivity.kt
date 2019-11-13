package com.ch.yoon.imagesearch.presentation.favorite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.databinding.ActivityFavoriteBinding
import com.ch.yoon.imagesearch.presentation.base.BaseActivity
import com.ch.yoon.imagesearch.presentation.imagedetail.ImageDetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteActivity : BaseActivity<ActivityFavoriteBinding>() {

    companion object {
        private const val REQUEST_CODE_IMAGE_DETAIL = 1
    }

    private val favoriteListViewModel: FavoriteListViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_favorite
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBackArrow()
        initFavoriteListViewModel()
        observeFavoriteListViewModel()
        initFavoriteRecyclerView()
    }

    private fun initBackArrow() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initFavoriteListViewModel() {
        binding.favoriteListViewModel = favoriteListViewModel

        if(isActivityFirstCreate) {
            favoriteListViewModel.loadFavoriteImageList()
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
                startActivityForResult(imageDetailIntent, REQUEST_CODE_IMAGE_DETAIL)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CODE_IMAGE_DETAIL) {
            if(resultCode == Activity.RESULT_OK) {
                data?.getParcelableExtra<ImageDocument>(ImageDetailActivity.EXTRA_IMAGE_DOCUMENT_KEY)
                    ?.let { updatedImageDocument ->
                        favoriteListViewModel.updateFavorite(updatedImageDocument)
                    }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        favoriteListViewModel.onClickBackPress()
    }
}
