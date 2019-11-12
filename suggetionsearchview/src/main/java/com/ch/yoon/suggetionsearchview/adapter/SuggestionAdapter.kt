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
    internal var onSuggestionSubButtonClick: ((text: String, position: Int) -> Unit)? = null
    internal var onSuggestionFooterClick: (() -> Unit)? = null

    protected fun onSuggestionItemClick(text:String, position: Int) {
        onSuggestionItemClick?.invoke(text, position)
    }

    protected fun onSuggestionSubButtonClick(text:String, position: Int) {
        onSuggestionSubButtonClick?.invoke(text, position)
    }

    protected fun onSuggestionFooterClick() {
        onSuggestionFooterClick?.invoke()
    }

}