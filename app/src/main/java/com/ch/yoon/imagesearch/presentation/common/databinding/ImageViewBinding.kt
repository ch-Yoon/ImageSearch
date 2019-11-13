package com.ch.yoon.imagesearch.presentation.common.databinding

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.ch.yoon.imagesearch.extension.loadImageWithCenterCrop
import com.ch.yoon.imagesearch.extension.loadImageWithCenterInside

/**
 * Creator : ch-yoon
 * Date : 2019-11-14.
 */
@BindingAdapter("loadImageWithCenterInside", "loadingProgressBar")
fun loadImageWithCenterInside(imageView: ImageView, imageUrl: String?, progressBar: ProgressBar) {
    imageView.loadImageWithCenterInside(imageUrl, progressBar)
}

@BindingAdapter("loadImageWithCenterCrop")
fun loadImageWithCenterCrop(imageView: ImageView, imageUrl: String?) {
    imageView.loadImageWithCenterCrop(imageUrl)
}

@BindingAdapter("srcAlpha")
fun setSrcAlpha(imageView: ImageView, alpha: Int) {
    imageView.drawable?.alpha = alpha
}