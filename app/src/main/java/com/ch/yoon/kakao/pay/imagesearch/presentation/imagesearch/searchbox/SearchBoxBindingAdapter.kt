package com.ch.yoon.kakao.pay.imagesearch.presentation.imagesearch.searchbox

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.data.repository.model.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.presentation.common.customview.searchview.SuggestionSearchView
import com.ch.yoon.kakao.pay.imagesearch.util.extension.hideKeyboard
import kotlinx.android.synthetic.main.suggestion_search_view.view.*
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */

@BindingAdapter("searchLogList")
fun setItems(suggestionSearchView: SuggestionSearchView, searchLogList: List<SearchLog>?) {
    val adapter = suggestionSearchView.suggestionRecyclerView.adapter as SearchLogAdapter?
    adapter?.submitList(if (searchLogList == null) null else ArrayList(searchLogList))
}

@BindingAdapter("hideKeyboard")
fun hideKeyboard(editText: EditText, isFocus: Boolean?) {
    if(isFocus == null || !isFocus) {
        editText.hideKeyboard()
    }
}

@BindingAdapter("setText")
fun setText(editText: EditText, text: String?) {
    if(editText.text.toString() != text) {
        editText.setText(text ?: "")
    }
}