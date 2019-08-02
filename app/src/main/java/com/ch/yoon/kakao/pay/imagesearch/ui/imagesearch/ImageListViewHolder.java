package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemImageListBinding;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;

/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListViewHolder extends RecyclerView.ViewHolder {

    private final ItemImageListBinding binding;

    public ImageListViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void setItem(@NonNull ImageInfo imageInfo) {
        binding.setImageInfo(imageInfo);
    }

}
