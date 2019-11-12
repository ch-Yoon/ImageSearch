package com.ch.yoon.imagesearch.presentation.imagesearch.searchbox

import androidx.databinding.BindingAdapter
import com.ch.yoon.suggetionsearchview.SuggestionSearchView
import com.ch.yoon.suggetionsearchview.adapter.DefaultSuggestionAdapter
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
@BindingAdapter("searchLogList")
fun setItems(suggestionSearchView: SuggestionSearchView, searchLogList: List<String>?) {
    (suggestionSearchView.getAdapter() as DefaultSuggestionAdapter?)?.let { adapter ->
        val newList = if(searchLogList == null || searchLogList.isEmpty()) {
            null
        } else {
            ArrayList(searchLogList)
        }

        adapter.submitList(newList)

        if(newList == null) {
            adapter.notifyDataSetChanged()
        }
    }
}

@BindingAdapter("close")
fun close(suggestionSearchView: SuggestionSearchView, closeEvent: Unit?) {
    suggestionSearchView.hide()
}