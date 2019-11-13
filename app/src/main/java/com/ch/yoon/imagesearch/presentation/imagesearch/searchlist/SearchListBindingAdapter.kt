package com.ch.yoon.imagesearch.presentation.imagesearch.searchlist

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.presentation.imagesearch.searchlist.SearchListViewModel.ImageSearchState

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
@BindingAdapter("imageSearchState")
fun setImageSearchState(recyclerView: RecyclerView, imageSearchState: ImageSearchState) {
    (recyclerView.adapter as SearchListAdapter?)?.let { adapter ->
        with(adapter) {
            when (imageSearchState) {
                ImageSearchState.NONE, ImageSearchState.SUCCESS -> {
                    changeFooterViewVisibility(false)
                }
                ImageSearchState.FAIL -> {
                    changeFooterViewVisibility(true)
                }
            }
        }
    }
}