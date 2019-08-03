package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.adapter.ImageListAdapter;
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListBindingAdapter {

    @BindingAdapter("countOfItemInLine")
    public static void setSpanCount(@NonNull RecyclerView recyclerView,
                                    @Nullable Integer countOfItemInLine) {
        GridLayoutManager gridLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
        if(gridLayoutManager != null) {
            final int spanCount = countOfItemInLine == null ? 3 : countOfItemInLine;
            gridLayoutManager.setSpanCount(spanCount);
        }
    }

    @BindingAdapter("searchImageInfoList")
    public static void setItems(@NonNull RecyclerView recyclerView,
                                @Nullable List<ImageInfo> imageInfoList) {
        ImageListAdapter adapter = ((ImageListAdapter)recyclerView.getAdapter());
        if(adapter != null) {
            adapter.submitList(imageInfoList);
        }
    }

    @BindingAdapter("loadImage")
    public static void loadImage(@NonNull ImageView imageView, @Nullable ImageInfo imageInfo) {
        if(imageInfo != null) {
            String thumbnailUrl = imageInfo.getThumbnailUrl();
            GlideUtil.load(imageView, thumbnailUrl);
        }
    }

}
