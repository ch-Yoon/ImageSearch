package com.ch.yoon.suggetionsearchview.extention

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Creator : ch-yoon
 * Date : 2019-11-12.
 */
internal fun View.visible() {
    visibility = View.VISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}

internal fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, 0)
}

internal fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}