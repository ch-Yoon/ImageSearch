package com.ch.yoon.suggetionsearchview.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
abstract class SuggestionAdapter<T, VH: RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback){

    internal var onSuggestionItemClick: ((text: String, position: Int) -> Unit)? = null
    internal var onSuggestionDeleteClick: ((text: String, position: Int) -> Unit)? = null

    protected fun onSuggestionItemClick(text:String, position: Int) {
        onSuggestionItemClick?.invoke(text, position)
    }

    protected fun onSuggestionDeleteClick(text:String, position: Int) {
        onSuggestionDeleteClick?.invoke(text, position)
    }

}