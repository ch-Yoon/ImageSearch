package com.ch.yoon.imagesearch.util.common

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ch.yoon.imagesearch.R

/**
 * Creator : ch-yoon
 * Date : 2019-10-29.
 */
private const val THUMBNAIL_VALUE = 0.1f

private val CENTER_CROP_REQUEST_OPTIONS = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .centerCrop()
    .error(R.drawable.image_load_fail)

private val CENTER_INSIDE_REQUEST_OPTIONS = RequestOptions()
    .diskCacheStrategy(DiskCacheStrategy.NONE)
    .centerInside()
    .error(R.drawable.image_load_fail)

fun cancelImageLoad(imageView: ImageView) {
    Glide.with(imageView.context).clear(imageView)
}

fun loadImageWithCenterCrop(imageView: ImageView, imageUrl: String?) {
    loadImage(imageView, imageUrl, CENTER_CROP_REQUEST_OPTIONS, null)
}

fun loadImageWithCenterInside(imageView: ImageView, imageUrl: String?, progressBar: ProgressBar) {
    loadImage(imageView, imageUrl, CENTER_INSIDE_REQUEST_OPTIONS, progressBar)
}

private fun loadImage(
    imageView: ImageView,
    imageUrl: String?,
    requestOptions: RequestOptions,
    progressBar: ProgressBar?
) {
    progressBar?.visibility = View.VISIBLE

    Glide.with(imageView.context)
        .load(imageUrl)
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
        .into(imageView)
}