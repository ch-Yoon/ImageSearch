package com.ch.yoon.imagesearch.presentation.common.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
/**
 * Creator : ch-yoon
 * Date : 2019-11-13.
 */
@Suppress("UNCHECKED_CAST")
@BindingAdapter("itemsWithListAdapter")
fun <T, VH : RecyclerView.ViewHolder> setItemsWithListAdapter(recyclerView: RecyclerView, items: List<T>?) {
    (recyclerView.adapter as? ListAdapter<T, VH>)?.let { adapter ->
        val newList = if(items == null || items.isEmpty()) null else ArrayList(items)
        adapter.submitList(newList)
        if(newList == null) {
            adapter.notifyDataSetChanged()
        }
    }
}

@BindingAdapter("spanCount")
fun setSpanCount(recyclerView: RecyclerView, spanCount: Int) {
    (recyclerView.layoutManager as? GridLayoutManager)?.spanCount = spanCount
}