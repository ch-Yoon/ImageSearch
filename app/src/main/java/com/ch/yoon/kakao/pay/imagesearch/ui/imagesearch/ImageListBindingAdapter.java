package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;

import java.util.List;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListBindingAdapter {

    @BindingAdapter("searchImageInfoList")
    public static void setItems(RecyclerView recyclerView, List<ImageInfo> imageInfoList) {
        ImageListAdapter adapter = ((ImageListAdapter)recyclerView.getAdapter());
        if(adapter != null) {
            adapter.submitList(imageInfoList);
        }
    }

}
