package com.ch.yoon.kakao.pay.imagesearch.util.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Creator : ch-yoon
 * Date : 2019-10-27.
 */
fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, 0)
}
fun View.visible() {
    if(visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.invisible() {
    if(visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}