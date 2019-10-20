package com.ch.yoon.kakao.pay.imagesearch.ui.imagedetail;

import android.widget.ProgressBar;

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

    @BindingAdapter({"loadImageWithCenterInside", "loadingProgressBar"})
    public static void loadImageWithCenterInside(@NonNull final PhotoView photoView,
                                                 @Nullable final String imageUrl,
                                                 @NonNull final ProgressBar progressBar) {
        GlideUtil.loadWithCenterInside(photoView, imageUrl, progressBar);
    }

}
