package com.ch.yoon.imagesearch.presentation.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.imagesearch.R
import com.ch.yoon.data.model.image.ImageDocument
import com.ch.yoon.imagesearch.databinding.ActivityImageDetailBinding
import com.ch.yoon.imagesearch.presentation.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
class ImageDetailActivity : BaseActivity<ActivityImageDetailBinding>() {

    companion object {
        const val EXTRA_IMAGE_DOCUMENT_KEY = "EXTRA_IMAGE_DOCUMENT_KEY"

        fun getImageDetailActivityIntent(context: Context, imageDocument: com.ch.yoon.data.model.image.ImageDocument): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_DOCUMENT_KEY, imageDocument)
            }
        }
    }

    private val imageDetailViewModel: ImageDetailViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_image_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initActionBar()

        initImageDetailViewModel()
    }

    private fun initActionBar() {
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initImageDetailViewModel() {
        binding.imageDetailViewModel = imageDetailViewModel

        val owner = this
        with(imageDetailViewModel) {
            if(isActivityFirstCreate) {
                val passedImageDocument = intent.getParcelableExtra<com.ch.yoon.data.model.image.ImageDocument>(EXTRA_IMAGE_DOCUMENT_KEY)
                imageDetailViewModel.showImageDetailInfo(passedImageDocument)
            }

            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToWebEvent.observe(owner, Observer { url ->
                val movieWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(movieWebIntent)
            })

            finishEvent.observe(owner, Observer {
                finish()
            })
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