package com.ch.yoon.imagesearch.presentation.common.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.SuggestionSearchView
import com.ch.yoon.suggetionsearchview.adapter.SuggestionAdapter

/**
 * Creator : ch-yoon
 * Date : 2019-11-14.
 */
@BindingAdapter("enable")
fun enable(suggestionSearchView: SuggestionSearchView, enable: Boolean) {
    if(enable) {
        suggestionSearchView.show()
    } else {
        suggestionSearchView.hide()
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("itemsWithDefaultAdapter")
fun <T, VH : RecyclerView.ViewHolder> setItemsWithListAdapter(suggestionSearchView: SuggestionSearchView, items: List<T>?) {
    (suggestionSearchView.getAdapter() as SuggestionAdapter<T, VH>?)?.let { adapter ->
        val newList = if(items == null || items.isEmpty()) null else ArrayList(items)
        adapter.submitList(newList)
        if(newList == null) {
            adapter.notifyDataSetChanged()
        }
    }
}