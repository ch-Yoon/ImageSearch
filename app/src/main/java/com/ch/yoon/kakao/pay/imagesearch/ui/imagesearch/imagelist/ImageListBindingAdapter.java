package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.ImageListAdapter;
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil;
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil;
import com.ch.yoon.kakao.pay.imagesearch.utils.IntegerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListBindingAdapter {

    @BindingAdapter("countOfItemInLine")
    public static void setSpanCount(@NonNull RecyclerView recyclerView,
                                    @NonNull Integer countOfItemInLine) {
        final GridLayoutManager gridLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
        if(gridLayoutManager != null) {
            gridLayoutManager.setSpanCount(countOfItemInLine);
        }
    }

    @BindingAdapter("searchImageInfoList")
    public static void setItems(@NonNull RecyclerView recyclerView,
                                @Nullable List<ImageDocument> simpleImageInfoList) {
        final ImageListAdapter adapter = ((ImageListAdapter)recyclerView.getAdapter());
        if(adapter != null) {
            adapter.submitList(simpleImageInfoList == null ? null : new ArrayList<>(simpleImageInfoList));

            if(CollectionUtil.isEmpty(simpleImageInfoList)) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @BindingAdapter("imageSearchState")
    public static void setImageSearchState(@NonNull RecyclerView recyclerView,
                                           @NonNull ImageSearchState imageSearchState) {
        final ImageListAdapter adapter = ((ImageListAdapter)recyclerView.getAdapter());
        if(adapter != null) {
            switch (imageSearchState) {
                case NONE:
                case SUCCESS:
                    adapter.changeFooterViewVisibility(false);
                    break;
                case FAIL:
                    adapter.changeFooterViewVisibility(true);
                    break;
            }
        }
    }

    @BindingAdapter("loadImageWithCenterCrop")
    public static void loadImageWithCenterCrop(@NonNull ImageView imageView,
                                               @Nullable String imageUrl) {
        GlideUtil.loadWithCenterCrop(imageView, imageUrl);
    }

    @BindingAdapter({"inputtedCountOfItemInLine", "expectedCountOfItemInLine"})
    public static void applySelectedState(@NonNull ImageView imageView,
                                          @Nullable Integer inputtedCountOfItemInLine,
                                          @Nullable Integer expectedCountOfItemInLine) {
        boolean isSelected = IntegerUtil.isSame(inputtedCountOfItemInLine, expectedCountOfItemInLine);
        imageView.setSelected(isSelected);
    }

}
