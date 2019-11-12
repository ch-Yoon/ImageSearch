package com.ch.yoon.suggetionsearchview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.R
import kotlinx.android.synthetic.main.item_suggestion.view.*
import kotlinx.android.synthetic.main.item_suggestion_footer.view.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class DefaultSuggestionAdapter : SuggestionAdapter<String, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val FOOTER = 0
        private const val ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FOOTER -> {
                SuggestionFooterViewHolder.create(parent).apply {
                    searchLogAllDeleteTextView.setOnClickListener {
                        onSuggestionFooterClick()
                    }
                }
            }
            else -> {
                SuggestionViewHolder.create(parent).apply {
                    keywordTextView.setOnClickListener {
                        onSuggestionItemClick(getItem(adapterPosition), adapterPosition)
                    }
                    deleteButton.setOnClickListener {
                        onSuggestionSubButtonClick(getItem(adapterPosition), adapterPosition)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is SuggestionViewHolder -> {
                holder.setSearchLog(getItem(position))
            }
        }
    }

    override fun getItemCount(): Int {
        val realItemCount = super.getItemCount()
        return if(realItemCount == 0) {
            realItemCount
        } else {
            realItemCount + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == super.getItemCount()) {
            FOOTER
        } else {
            ITEM
        }
    }

    class SuggestionFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val searchLogAllDeleteTextView: TextView = view.suggestion_footer_text_view

        companion object {
            fun create(parent: ViewGroup): SuggestionFooterViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_suggestion_footer, parent, false)
                return SuggestionFooterViewHolder(view)
            }
        }
    }

    class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val keywordTextView: TextView = view.suggestion_text_view
        val deleteButton: ImageView = view.suggestion_sub_button_image_view

        fun setSearchLog(keyword: String) {
            keywordTextView.text = keyword
        }

        companion object {
            fun create(parent: ViewGroup): SuggestionViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_suggestion, parent, false)
                return SuggestionViewHolder(view)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}
