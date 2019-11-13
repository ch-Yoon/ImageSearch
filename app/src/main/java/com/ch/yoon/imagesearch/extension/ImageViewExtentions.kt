package com.ch.yoon.imagesearch.extension

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ch.yoon.imagesearch.R

/**
 * Creator : ch-yoon
 * Date : 2019-11-13.
 */
private const val THUMBNAIL_VALUE = 0.1f

private val centerCropRequestOptionsMap = mutableMapOf<Int, RequestOptions>()
private val centerInsideRequestOptionsMap = mutableMapOf<Int, RequestOptions>()

private fun getCenterCropRequestOptions(roundedCornersRadius: Int): RequestOptions {
    val radius = if(roundedCornersRadius < 0) 0 else roundedCornersRadius
    val requestOptions = centerCropRequestOptionsMap[radius] ?: run {
        createDefaultRequestOptions().centerCrop().apply {
            if (0 < roundedCornersRadius) {
                transform(RoundedCorners(roundedCornersRadius))
            }
        }
    }
    centerCropRequestOptionsMap[radius] = requestOptions
    return requestOptions
}

private fun getCenterInsideRequestOptions(roundedCornersRadius: Int): RequestOptions {
    val radius = if(roundedCornersRadius < 0) 0 else roundedCornersRadius
    val requestOptions = centerInsideRequestOptionsMap[radius] ?: run {
        createDefaultRequestOptions().centerInside().apply {
            if (0 < roundedCornersRadius) {
                transform(RoundedCorners(roundedCornersRadius))
            }
        }
    }
    centerCropRequestOptionsMap[radius] = requestOptions
    return requestOptions
}

private fun createDefaultRequestOptions(): RequestOptions {
    return RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.drawable.image_load_fail)
}

fun ImageView.cancelImageLoad() {
    Glide.with(context).clear(this)
}

fun ImageView.loadImageWithCenterCrop(
    imageUrl: String?,
    roundedCornersRadius: Int = 0,
    progressBar: ProgressBar? = null
) {
    val requestOptions = getCenterCropRequestOptions(roundedCornersRadius)
    loadImage(imageUrl, requestOptions, progressBar)
}

fun ImageView.loadImageWithCenterInside(
    imageUrl: String?,
    roundedCornersRadius: Int = 0,
    progressBar: ProgressBar? = null
) {
    val requestOptions = getCenterInsideRequestOptions(roundedCornersRadius)
    loadImage(imageUrl, requestOptions, progressBar)
}

private fun ImageView.loadImage(
    imageUrl: String?,
    requestOptions: RequestOptions,
    progressBar: ProgressBar?
) {
    progressBar?.visibility = View.VISIBLE

    Glide.with(context)
        .load(imageUrl ?: "")
        .thumbnail(THUMBNAIL_VALUE)
        .transition(DrawableTransitionOptions.withCrossFade())
        .apply(requestOptions)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Drawable>,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: Target<Drawable>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }
        })
        .into(this)
}