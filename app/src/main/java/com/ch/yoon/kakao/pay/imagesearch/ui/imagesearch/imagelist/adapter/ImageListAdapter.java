package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.repository.model.imagesearch.response.SimpleImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.viewholder.RetryFooterViewHolder;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist.adapter.viewholder.ImageListViewHolder;

import java.util.Objects;


/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListAdapter extends ListAdapter<SimpleImageInfo, RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    @Nullable
    private OnBindPositionListener onBindPositionListener;
    @Nullable
    private OnListItemClickListener onListItemClickListener;
    @Nullable
    private OnRetryItemClickListener onRetryItemClickListener;

    private boolean retryFooterViewVisibility = false;

    public ImageListAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType == TYPE_ITEM) {
            final View itemView = inflater.inflate(R.layout.item_image_list, parent, false);
            final ImageListViewHolder itemViewHolder = new ImageListViewHolder(itemView);
            itemViewHolder.setOnListItemClickListener(onListItemClickListener);
            return itemViewHolder;
        } else {
            final View footerView = inflater.inflate(R.layout.item_retry_footer, parent, false);
            final RetryFooterViewHolder retryFooterViewHolder = new RetryFooterViewHolder(footerView);
            retryFooterViewHolder.setOnFooterItemClickListener(onRetryItemClickListener);
            return retryFooterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ImageListViewHolder) {
            ((ImageListViewHolder) holder).setItem(getItem(position));

            if (onBindPositionListener != null) {
                onBindPositionListener.onBindPosition(position);
            }
        } else {
            ((RetryFooterViewHolder) holder).setRetryVisibility(retryFooterViewVisibility);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isFooterViewPosition(position)) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if(holder instanceof ImageListViewHolder) {
            ((ImageListViewHolder) holder).clear();
        }

        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        int itemCount = super.getItemCount();
        if(itemCount == 0) {
            return itemCount;
        } else {
            return itemCount + 1;
        }
    }

    public boolean isFooterViewPosition(int position) {
        return position == super.getItemCount();
    }

    public void changeFooterViewVisibility(boolean retryFooterViewVisibility) {
        this.retryFooterViewVisibility = retryFooterViewVisibility;
        notifyItemChanged(super.getItemCount());
    }

    public void setOnBindPositionListener(@NonNull OnBindPositionListener onBindPositionListener) {
        this.onBindPositionListener = onBindPositionListener;
    }

    public void setOnListItemClickListener(@NonNull OnListItemClickListener onListItemClickListener) {
        this.onListItemClickListener = onListItemClickListener;
    }

    public void setOnFooterItemClickListener(@NonNull OnRetryItemClickListener onRetryItemClickListener) {
        this.onRetryItemClickListener = onRetryItemClickListener;
    }

    private static final DiffUtil.ItemCallback<SimpleImageInfo> DiffCallback = new DiffUtil.ItemCallback<SimpleImageInfo>() {

        @Override
        public boolean areItemsTheSame(@NonNull SimpleImageInfo oldItem, @NonNull SimpleImageInfo newItem) {
            return Objects.equals(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull SimpleImageInfo oldItem, @NonNull SimpleImageInfo newItem) {
            return oldItem.equals(newItem);
        }

    };

}
