package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemRetryFooterBinding;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.OnRetryItemClickListener;

/**
 * Creator : ch-yoon
 * Date : 2019-08-04.
 */
public class RetryFooterViewHolder extends RecyclerView.ViewHolder {

    private final ItemRetryFooterBinding binding;

    public RetryFooterViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }

    public void setRetryVisibility(boolean visible) {
        binding.setRetryButtonVisibility(visible);
    }

    public void setOnFooterItemClickListener(@Nullable OnRetryItemClickListener onRetryItemClickListener) {
        binding.retryButton.setOnClickListener(v -> {
            binding.retryButton.setVisibility(View.INVISIBLE);
            if(onRetryItemClickListener != null) {
                onRetryItemClickListener.onClick();
            }
        });
    }

}
