package com.ch.yoon.suggetionsearchview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.R
import com.ch.yoon.suggetionsearchview.model.SearchLog
import kotlinx.android.synthetic.main.item_search_log.view.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class SearchLogAdapter : SuggestionAdapter<SearchLog, SearchLogAdapter.SearchLogViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_log, parent, false)
        return SearchLogViewHolder(view).apply {
            keywordTextView.setOnClickListener {
                onSuggestionItemClick(getItem(adapterPosition).keyword, adapterPosition)
            }
            deleteButton.setOnClickListener {
                onSuggestionDeleteClick(getItem(adapterPosition).keyword, adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: SearchLogViewHolder, position: Int) {
        holder.setSearchLog(getItem(position))
    }

    class SearchLogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val keywordTextView: TextView
        val deleteButton: ImageView

        init {
            with(view) {
                keywordTextView = keyword_text_view
                deleteButton = keyword_delete_button
            }
        }

        fun setSearchLog(searchLog: SearchLog) {
            keywordTextView.text = searchLog.keyword
        }
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
