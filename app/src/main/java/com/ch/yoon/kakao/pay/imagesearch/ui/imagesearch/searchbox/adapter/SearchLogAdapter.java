package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ch.yoon.kakao.pay.imagesearch.R;
import com.ch.yoon.kakao.pay.imagesearch.repository.local.room.entity.SearchLog;
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.adapter.viewholder.SearchLogViewHolder;


/**
 * Creator : ch-yoon
 * Date : 2019-08-02.
 */
public class SearchLogAdapter extends ListAdapter<SearchLog, SearchLogViewHolder> {

    @Nullable
    private OnSearchLogClickListener onSearchLogClickListener;

    @Nullable
    private OnLogDeleteClickListener onLogDeleteClickListener;

    public SearchLogAdapter() {
        super(DiffCallback);
    }

    @NonNull
    @Override
    public SearchLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View itemView = inflater.inflate(R.layout.item_search_history, parent, false);
        final SearchLogViewHolder viewHolder = new SearchLogViewHolder(itemView);

        viewHolder.setOnSearchLogClickListener(onSearchLogClickListener);
        viewHolder.setOnLogDeleteClickListener(onLogDeleteClickListener);

        return new SearchLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchLogViewHolder holder, int position) {
        holder.setSearchLog(getItem(position));
    }

    public void setOnSearchLogClickListener(@Nullable OnSearchLogClickListener onSearchLogClickListener) {
        this.onSearchLogClickListener = onSearchLogClickListener;
    }

    public void setOnLogDeleteClickListener(@Nullable OnLogDeleteClickListener onLogDeleteClickListener) {
        this.onLogDeleteClickListener = onLogDeleteClickListener;
    }

    private static final DiffUtil.ItemCallback<SearchLog> DiffCallback = new DiffUtil.ItemCallback<SearchLog>() {

        @Override
        public boolean areItemsTheSame(@NonNull SearchLog oldItem, @NonNull SearchLog newItem) {
            return oldItem.getKeyword().equals(newItem.getKeyword());
        }

        @Override
        public boolean areContentsTheSame(@NonNull SearchLog oldItem, @NonNull SearchLog newItem) {
            return oldItem.equals(newItem);
        }

    };

}
