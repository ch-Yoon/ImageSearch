package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.repository.remote.kakao.response.imagesearch.ImageInfo;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.adapter.viewholder.ImageListViewHolder;

import java.util.Objects;


/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class ImageListAdapter extends ListAdapter<ImageInfo, ImageListViewHolder> {

    @Nullable
    private OnBindPositionListener onBindPositionListener;

    public ImageListAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public ImageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_image_list, parent, false);

        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageListViewHolder holder, int position) {
        holder.setItem(getItem(position));

        if(onBindPositionListener != null) {
            onBindPositionListener.onBindPosition(position);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ImageListViewHolder holder) {
        holder.clear();
        super.onViewRecycled(holder);
    }

    public void setOnBindPositionListener(@Nullable OnBindPositionListener onBindPositionListener) {
        this.onBindPositionListener = onBindPositionListener;
    }

    private static final DiffUtil.ItemCallback<ImageInfo> DiffCallback = new DiffUtil.ItemCallback<ImageInfo>() {

        @Override
        public boolean areItemsTheSame(@NonNull ImageInfo oldItem, @NonNull ImageInfo newItem) {
            return Objects.equals(oldItem.getImageUrl(), newItem.getImageUrl()) &&
                Objects.equals(oldItem.getThumbnailUrl(), newItem.getThumbnailUrl());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ImageInfo oldItem, @NonNull ImageInfo newItem) {
            return oldItem.equals(newItem);
        }

    };

}
