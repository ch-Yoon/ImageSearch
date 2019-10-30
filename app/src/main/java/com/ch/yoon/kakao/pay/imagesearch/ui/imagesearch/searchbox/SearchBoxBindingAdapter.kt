package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.searchbox

import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.data.source.local.room.entity.SearchLogModel
import com.ch.yoon.kakao.pay.imagesearch.extention.hideKeyboard
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */

@BindingAdapter("searchLogModelList")
fun setItems(recyclerView: RecyclerView, searchLogModelList: List<SearchLogModel>?) {
    val adapter = recyclerView.adapter as SearchLogAdapter?
    adapter?.submitList(if (searchLogModelList == null) null else ArrayList(searchLogModelList))
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