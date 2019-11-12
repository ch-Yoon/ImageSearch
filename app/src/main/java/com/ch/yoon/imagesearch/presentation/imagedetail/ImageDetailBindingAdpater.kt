package com.ch.yoon.imagesearch.presentation.imagedetail

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.ch.yoon.imagesearch.util.common.loadImageWithCenterInside
import com.github.chrisbanes.photoview.PhotoView

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
@BindingAdapter("loadImageWithCenterInside", "loadingProgressBar")
fun loadImageWithCenterInside(photoView: PhotoView, imageUrl: String?, progressBar: ProgressBar) {
    loadImageWithCenterInside(photoView, imageUrl, progressBar)
}
