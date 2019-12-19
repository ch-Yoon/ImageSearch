package com.ch.yoon.imagesearch.presentation.common.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.suggetionsearchview.SuggestionSearchView

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

@BindingAdapter("suggestionsEnable")
fun suggestionsEnable(suggestionSearchView: SuggestionSearchView, enable: Boolean) {
    if(enable) {
        suggestionSearchView.showSuggestions()
    } else {
        suggestionSearchView.hideSuggestions()
    }
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("itemsWithListAdapter")
fun <T, VH : RecyclerView.ViewHolder> setItemsWithListAdapter(suggestionSearchView: SuggestionSearchView, items: List<T>?) {
    (suggestionSearchView.getAdapter() as? ListAdapter<T, VH>)?.let { adapter ->
        val newList = if(items == null || items.isEmpty()) null else ArrayList(items)
        adapter.submitList(newList)
        if(newList == null) {
            adapter.notifyDataSetChanged()
        }
    }
}