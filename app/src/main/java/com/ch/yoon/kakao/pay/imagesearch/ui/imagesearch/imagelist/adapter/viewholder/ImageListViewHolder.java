package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemImageListBinding;
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.OnListItemClickListener;
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil;

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

    public void setItem(@NonNull ImageDocument imageDocument) {
        binding.setImageInfo(imageDocument);
    }

    public void setOnListItemClickListener(@Nullable OnListItemClickListener onListItemClickListener) {
        binding.imageView.setOnClickListener(v -> {
            if(onListItemClickListener != null) {
                onListItemClickListener.onClick(binding.getImageInfo(), getAdapterPosition());
            }
        });
    }

    public void clear() {
        GlideUtil.cancel(binding.imageView);
    }

}
