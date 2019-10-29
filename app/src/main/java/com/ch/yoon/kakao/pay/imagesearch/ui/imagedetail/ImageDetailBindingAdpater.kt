package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail

import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil
import com.github.chrisbanes.photoview.PhotoView

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
@BindingAdapter("loadImageWithCenterInside", "loadingProgressBar")
fun loadImageWithCenterInside(photoView: PhotoView, imageUrl: String?, progressBar: ProgressBar) {
    GlideUtil.loadWithCenterInside(photoView, imageUrl, progressBar)
}
