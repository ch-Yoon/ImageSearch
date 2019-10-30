package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response.KakaoImageDocument
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

        fun getImageDetailActivityIntent(context: Context, kakaoImageDocument: KakaoImageDocument): Intent {
            return Intent(context, ImageDetailActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_DOCUMENT_KEY, kakaoImageDocument)
            }
        }
    }

    private val imageDetailViewModel: ImageDetailViewModel by viewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_image_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val passedImageDocument = intent.getParcelableExtra<KakaoImageDocument>(EXTRA_IMAGE_DOCUMENT_KEY)

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

    private fun initImageDetailViewModel(kakaoImageDocument: KakaoImageDocument) {
        binding.imageDetailViewModel = imageDetailViewModel
        if(isActivityFirstCreate) {
            imageDetailViewModel.showImageDetailInfo(kakaoImageDocument)
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

            finishEvent.observe(owner, Observer {
                finish()
            })
        }
    }

    override fun onBackPressed() {
        imageDetailViewModel.onClickBackPress()
    }
}