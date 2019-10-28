package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.data.local.room.entity.SearchLog
import com.ch.yoon.kakao.pay.imagesearch.extention.hideKeyboard
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */

@BindingAdapter("searchLogList")
fun setItems(recyclerView: RecyclerView, searchLogList: List<SearchLog>?) {
    val adapter = recyclerView.adapter as SearchLogAdapter?
    adapter?.submitList(if (searchLogList == null) null else ArrayList(searchLogList))
}

@BindingAdapter("hideKeyboard")
fun hideKeyboard(editText: EditText, isFocus: Boolean?) {
    if(isFocus == null || !isFocus) {
        editText.hideKeyboard()
    }
}