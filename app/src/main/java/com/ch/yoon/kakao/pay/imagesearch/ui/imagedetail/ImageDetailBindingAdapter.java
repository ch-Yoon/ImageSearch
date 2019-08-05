package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil;
import com.github.chrisbanes.photoview.PhotoView;

/**
 * Creator : ch-yoon
 * Date : 2019-08-05.
 */
public class ImageDetailBindingAdapter {

    @BindingAdapter("loadImageWithCenterInside")
    public static void loadImageWithCenterInside(@NonNull PhotoView photoView,
                                                 @Nullable String imageUrl) {
        GlideUtil.loadWithCenterInside(photoView, imageUrl);
    }

}
