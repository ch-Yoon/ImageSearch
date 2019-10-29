package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.databinding.ActivityImageDetailBinding
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
class ImageDetailActivity : BaseActivity<ActivityImageDetailBinding>() {

    companion object {
        const val EXTRA_IMAGE_DOCUMENT_KEY = "EXTRA_IMAGE_DOCUMENT_KEY"

        fun getImageDetailActivityIntent(context: Context, imageDocument: ImageDocument): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(ImageDetailActivity.EXTRA_IMAGE_DOCUMENT_KEY, imageDocument)
            }
        }
    }

    private val imageDetailViewModel: ImageDetailViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_image_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passedImageDocument = intent.getParcelableExtra<ImageDocument>(EXTRA_IMAGE_DOCUMENT_KEY)

        initBackArrow()
        initImageDetailViewModel(passedImageDocument)
        observeImageDetailViewModel()
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

    private fun initBackArrow() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initImageDetailViewModel(imageDocument: ImageDocument) {
        binding.imageDetailViewModel = imageDetailViewModel
        if(isActivityFirstCreate) {
            imageDetailViewModel.showImageDetailInfo(imageDocument)
        }
    }

    private fun observeImageDetailViewModel() {
        binding.imageDetailViewModel
            .observeShowMessage()
            .observe(this, Observer { this.showToast(it) })

        binding.imageDetailViewModel
            .observeMoveWebEvent()
            .observe(this, Observer { url ->
                val webUri = Uri.parse(url)
                val movieWebIntent = Intent(Intent.ACTION_VIEW, webUri)
                startActivity(movieWebIntent) })

        binding.imageDetailViewModel
            .observeFinishEvent()
            .observe(this, Observer { finish() })
    }

    override fun onBackPressed() {
        binding.imageDetailViewModel.onClickBackPress()
    }

}