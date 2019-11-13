package com.ch.yoon.imagesearch.presentation.imagedetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
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

        fun getImageDetailActivityIntent(context: Context, imageDocument: ImageDocument): Intent {
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
        val owner = this
        with(imageDetailViewModel) {
            showMessageEvent.observe(owner, Observer { message ->
                showToast(message)
            })

            moveToWebEvent.observe(owner, Observer { url ->
                val movieWebIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(movieWebIntent)
            })

            finishEventWithNotUpdate.observe(owner, Observer {
                setResult(Activity.RESULT_CANCELED)
                finish()
            })

            finishEventWithUpdate.observe(owner, Observer { updatedImageDocument ->
                val intent = Intent().apply { putExtra(EXTRA_IMAGE_DOCUMENT_KEY, updatedImageDocument) }
                setResult(Activity.RESULT_OK, intent)
                finish()
            })
        }
    }

    override fun onBackPressed() {
        imageDetailViewModel.onClickBackPress()
    }
}