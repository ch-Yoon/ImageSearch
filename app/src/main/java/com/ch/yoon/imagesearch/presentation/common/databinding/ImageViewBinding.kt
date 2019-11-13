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
@BindingAdapter(value = ["loadImageWithCenterInside", "roundedCorners", "loadingProgressBar"], requireAll = false)
fun loadImageWithCenterInside(
    imageView: ImageView,
    imageUrl: String?,
    roundedCorners: Int = 0,
    progressBar: ProgressBar? = null
) {
    imageView.loadImageWithCenterInside(imageUrl, roundedCorners, progressBar)
}

@BindingAdapter(value = ["loadImageWithCenterCrop", "roundedCorners", "loadingProgressBar"], requireAll = false)
fun loadImageWithCenterCrop(
    imageView: ImageView,
    imageUrl: String?,
    roundedCorners: Int = 0,
    progressBar: ProgressBar? = null
) {
    imageView.loadImageWithCenterCrop(imageUrl, roundedCorners, progressBar)
}

@BindingAdapter("srcAlpha")
fun setSrcAlpha(imageView: ImageView, alpha: Int) {
    imageView.drawable?.alpha = alpha
}