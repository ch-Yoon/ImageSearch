package com.ch.yoon.imagesearch.extension

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
 * Date : 2019-11-13.
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

fun ImageView.cancelImageLoad() {
    Glide.with(context).clear(this)
}

fun ImageView.loadImageWithCenterCrop(imageUrl: String?) {
    loadImage(imageUrl, CENTER_CROP_REQUEST_OPTIONS, null)
}

fun ImageView.loadImageWithCenterInside(imageUrl: String?, progressBar: ProgressBar) {
    loadImage(imageUrl, CENTER_INSIDE_REQUEST_OPTIONS, progressBar)
}

private fun ImageView.loadImage(
    imageUrl: String?,
    requestOptions: RequestOptions,
    progressBar: ProgressBar?
) {
    progressBar?.visibility = View.VISIBLE

    Glide.with(context)
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
        .into(this)
}