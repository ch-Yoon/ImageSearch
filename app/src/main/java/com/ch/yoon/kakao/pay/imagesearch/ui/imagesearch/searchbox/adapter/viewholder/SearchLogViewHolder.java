package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemSearchHistoryBinding;
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.OnLogDeleteClickListener;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.OnSearchLogClickListener;

/**
 * Creator : ch-yoon
 * Date : 2019-08-04.
 */
public class SearchLogViewHolder extends RecyclerView.ViewHolder {

    private final ItemSearchHistoryBinding binding;

    public SearchLogViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void setSearchLog(@NonNull SearchLog searchLog) {
        binding.setSearchLog(searchLog);
    }

    public void setOnSearchLogClickListener(@Nullable OnSearchLogClickListener onSearchLogClickListener) {
        binding.keywordTextView.setOnClickListener(v -> {
            if(onSearchLogClickListener != null) {
                onSearchLogClickListener.onClick(binding.getSearchLog(), getAdapterPosition());
            }
        });
    }

    public void setOnLogDeleteClickListener(@Nullable OnLogDeleteClickListener onLogDeleteClickListener) {
        binding.logDeleteButton.setOnClickListener(v -> {
            if(onLogDeleteClickListener != null) {
                onLogDeleteClickListener.onClick(binding.getSearchLog(), getAdapterPosition());
            }
        });
    }

}
