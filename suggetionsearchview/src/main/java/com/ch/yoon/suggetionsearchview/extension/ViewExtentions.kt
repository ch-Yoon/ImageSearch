package com.ch.yoon.suggetionsearchview.extension

import android.content.Context
import android.view.View
import android.view.View.*
import android.view.inputmethod.InputMethodManager

/**
 * Creator : ch-yoon
 * Date : 2019-11-07
 **/
fun View.hideKeyboard() {
    if(isFocusable) {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}

fun View.visible() {
    if(visibility != VISIBLE) {
        visibility = VISIBLE
    }
}

fun View.invisible() {
    if(visibility != INVISIBLE) {
        visibility = INVISIBLE
    }
}