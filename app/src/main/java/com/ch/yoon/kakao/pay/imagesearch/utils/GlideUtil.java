package com.ch.yoon.kakao.pay.imagesearch.utils;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.ch.yoon.kakao.pay.imagesearch.R;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class GlideUtil {

    private static final float THUMBNAIL_VALUE = 0.1f;

    public static void load(@NonNull ImageView imageView, @Nullable String imageUrl) {
        Glide.with(imageView.getContext())
            .load(imageUrl)
            .thumbnail(THUMBNAIL_VALUE)
            .transition(DrawableTransitionOptions.withCrossFade())
            .apply(new RequestOptions()
                .transform(new CenterCrop())
                .error(R.drawable.image_load_fail)
            )
            .into(imageView);
    }

    public static void cancel(@NonNull ImageView imageView) {
        Glide.with(imageView.getContext()).clear(imageView);
    }

}