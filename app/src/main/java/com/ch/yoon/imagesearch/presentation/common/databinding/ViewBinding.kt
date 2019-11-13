package com.ch.yoon.imagesearch.presentation.common.databinding

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Creator : ch-yoon
 * Date : 2019-11-14.
 */
@BindingAdapter("isSelected")
fun isSelected(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}