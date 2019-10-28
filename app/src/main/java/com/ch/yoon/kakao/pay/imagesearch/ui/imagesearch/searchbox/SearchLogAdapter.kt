package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemSearchHistoryBinding
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewHolder
import com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox.SearchLogAdapter.SearchLogViewHolder

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
class SearchLogAdapter(
    private val searchLogViewModel: SearchBoxViewModel
) : ListAdapter<SearchLog, SearchLogViewHolder>(SearchLogDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLogViewHolder {
        return SearchLogViewHolder(searchLogViewModel, R.layout.item_search_history, parent)
    }

    override fun onBindViewHolder(holder: SearchLogViewHolder, position: Int) {
        holder.setSearchLog(getItem(position))
    }

    class SearchLogViewHolder(
        viewModel: SearchBoxViewModel,
        @LayoutRes layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemSearchHistoryBinding>(layoutResId, parent) {

        init {
            binding.viewModel = viewModel
        }

        fun setSearchLog(searchLog: SearchLog) {
            binding.searchLog = searchLog
            binding.executePendingBindings()
        }
    }

    class SearchLogDiffCallback : DiffUtil.ItemCallback<SearchLog>() {
        override fun areItemsTheSame(oldItem: SearchLog, newItem: SearchLog): Boolean {
            return oldItem.keyword == newItem.keyword
        }

        override fun areContentsTheSame(oldItem: SearchLog, newItem: SearchLog): Boolean {
            return oldItem == newItem
        }
    }
}

