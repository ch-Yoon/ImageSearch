package com.ch.yoon.kakao.pay.imagesearch.extention

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
fun View.hideKeyboard() {
    if(isFocusable) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}