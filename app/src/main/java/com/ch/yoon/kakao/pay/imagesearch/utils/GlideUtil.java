package com.ch.yoon.kakao.pay.imagesearch.utils;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;

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
        load(imageView, imageUrl, CENTER_CROP_REQUEST_OPTIONS);
    }

    public static void loadWithCenterInside(@NonNull ImageView imageView, @Nullable String imageUrl) {
        load(imageView, imageUrl, CENTER_INSIDE_REQUEST_OPTIONS);
    }

    private static void load(@NonNull ImageView imageView,
                             @Nullable String imageUrl,
                             @NonNull RequestOptions requestOptions) {
        Glide.with(imageView.getContext())
            .load(imageUrl)
            .thumbnail(THUMBNAIL_VALUE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(requestOptions)
            .into(imageView);
    }



}