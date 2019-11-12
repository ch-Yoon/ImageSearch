package com.ch.yoon.suggetionsearchview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.R
import com.ch.yoon.suggetionsearchview.extention.gone
import kotlinx.android.synthetic.main.item_suggestion.view.*
import kotlinx.android.synthetic.main.item_suggestion_footer.view.*

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
class DefaultSuggestionAdapter : SuggestionAdapter<String, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        const val FOOTER = 0
        const val ITEM = 1
    }

    var hasFooterView: Boolean = false
    var accessoryIconResId: Int = -1
    var itemTextSize: Float = -1f
    var itemTextColor: Int = -1
    var subButtonIconResId: Int = -1
    var footerText: String = ""
    var footerTextSize: Float = -1f
    var footerTextColor: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FOOTER -> {
                SuggestionFooterViewHolder.create(parent).apply {
                    footerTextView.apply {
                        text = footerText
                        if(footerTextSize != -1f) {
                            textSize = footerTextSize
                        }
                        if(footerTextColor != -1) {
                            setTextColor(footerTextColor)
                        }
                        setOnClickListener {
                            onSuggestionFooterClick?.invoke()
                        }
                    }
                }
            }
            else -> {
                SuggestionViewHolder.create(parent).apply {
                    accessoryButton.apply {
                        if(accessoryIconResId != -1) {
                            setImageResource(accessoryIconResId)
                        } else {
                            gone()
                        }
                    }

                    keywordTextView.apply {
                        if(itemTextSize != -1f) {
                            textSize = itemTextSize
                        }
                        if(itemTextColor != -1) {
                            setTextColor(itemTextColor)
                        }
                        setOnClickListener {
                            onSuggestionItemClick?.invoke(getItem(adapterPosition), adapterPosition)
                        }
                    }

                    subButton.apply {
                        if(subButtonIconResId != -1) {
                            setImageResource(subButtonIconResId)

                            setOnClickListener {
                                onSuggestionSubButtonClick?.invoke(getItem(adapterPosition), adapterPosition)
                            }
                        } else {
                            gone()
                        }
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
        return if(realItemCount == 0 || hasFooterView.not()) {
            realItemCount
        } else {
            realItemCount + 1
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasFooterView && position == super.getItemCount()) {
            FOOTER
        } else {
            ITEM
        }
    }

    class SuggestionFooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val footerTextView: TextView = view.suggestion_footer_text_view

        companion object {
            fun create(parent: ViewGroup): SuggestionFooterViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_suggestion_footer, parent, false)
                return SuggestionFooterViewHolder(view)
            }
        }
    }

    class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val accessoryButton: ImageView = view.suggestion_accessory_image_view
        val keywordTextView: TextView = view.suggestion_text_view
        val subButton: ImageView = view.suggestion_sub_button_image_view

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
