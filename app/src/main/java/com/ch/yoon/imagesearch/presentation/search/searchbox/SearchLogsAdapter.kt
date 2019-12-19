package com.ch.yoon.imagesearch.presentation.search.searchbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.data.repository.searchlog.model.SearchLog
import com.ch.yoon.imagesearch.databinding.ItemAllDeleteFooterBinding
import com.ch.yoon.imagesearch.databinding.ItemSearchLogBinding
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Creator : ch-yoon
 * Date : 2019-12-19.
 */
class SearchLogsAdapter : ListAdapter<SearchLog, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    private val _itemClicks = PublishSubject.create<SearchLog>()
    val itemClicks: Observable<SearchLog> = _itemClicks

    private val _itemDeleteClicks = PublishSubject.create<SearchLog>()
    val itemDeleteClicks: Observable<SearchLog> = _itemDeleteClicks

    private val _footerClicks = PublishSubject.create<Unit>()
    val footerClicks: Observable<Unit> = _footerClicks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            SearchLogViewHolder(ItemSearchLogBinding.inflate(inflater, parent, false)).apply {
                binding.searchLogTextView.setOnClickListener {
                    _itemClicks.onNext(getItem(adapterPosition))
                }
                binding.searchLogDeleteButton.setOnClickListener {
                    _itemDeleteClicks.onNext(getItem(adapterPosition))
                }
            }
        } else {
            AllDeleteFooterViewHolder(ItemAllDeleteFooterBinding.inflate(inflater, parent, false)).apply {
                binding.searchLogDeleteAllButton.setOnClickListener {
                    _footerClicks.onNext(Unit)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SearchLogViewHolder -> {
                holder.setItem(getItem(position))
            }
        }
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        return if (itemCount == 0) {
            itemCount
        } else {
            itemCount + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isFooterViewPosition(position)) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    private fun isFooterViewPosition(position: Int): Boolean {
        return position == super.getItemCount()
    }

    class SearchLogViewHolder(
        val binding: ItemSearchLogBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(searchLog: SearchLog) {
            binding.searchLog = searchLog
        }
    }

    class AllDeleteFooterViewHolder(
        val binding: ItemAllDeleteFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    class DiffCallback : DiffUtil.ItemCallback<SearchLog>() {
        override fun areItemsTheSame(oldItem: SearchLog, newItem: SearchLog): Boolean {
            return oldItem.keyword == newItem.keyword
        }

        override fun areContentsTheSame(oldItem: SearchLog, newItem: SearchLog): Boolean {
            return oldItem == newItem
        }
    }
}