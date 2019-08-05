package com.ch.yoon.kakao.pay.imagesearch.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.ch.yoon.kakao.pay.imagesearch.R;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class GlideUtil {

    private static final float THUMBNAIL_VALUE = 0.1f;

    private static final RequestOptions CENTER_CROP_REQUEST_OPTIONS = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.image_load_fail);

    private static final RequestOptions CENTER_INSIDE_REQUEST_OPTIONS = new RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.image_load_fail);

    public static void cancel(@NonNull ImageView imageView) {
        Glide.with(imageView.getContext()).clear(imageView);
    }

    public static void loadWithCenterCrop(@NonNull ImageView imageView, @Nullable String imageUrl) {
        load(imageView, imageUrl, CENTER_CROP_REQUEST_OPTIONS, null);
    }

    public static void loadWithCenterInside(@NonNull ImageView imageView,
                                            @Nullable String imageUrl,
                                            @NonNull ProgressBar progressBar) {
        load(imageView, imageUrl, CENTER_INSIDE_REQUEST_OPTIONS, progressBar);
    }

    private static void load(@NonNull ImageView imageView,
                             @Nullable String imageUrl,
                             @NonNull RequestOptions requestOptions,
                             @Nullable ProgressBar progressBar) {
        if(progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }

        Glide.with(imageView.getContext())
            .load(imageUrl)
            .thumbnail(THUMBNAIL_VALUE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    if(progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    if(progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    return false;
                }
            })
            .into(imageView);
    }

}