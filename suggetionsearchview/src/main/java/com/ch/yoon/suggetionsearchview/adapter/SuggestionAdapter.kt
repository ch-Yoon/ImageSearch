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

    var onSuggestionItemClick: ((text: String, position: Int) -> Unit)? = null
    var onSuggestionSubButtonClick: ((text: String, position: Int) -> Unit)? = null
    var onSuggestionFooterClick: (() -> Unit)? = null

}