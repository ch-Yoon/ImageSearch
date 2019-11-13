package com.ch.yoon.imagesearch.presentation.favorite

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-11-13
 **/
@BindingAdapter("favoriteImageList")
fun setItems(recyclerView: RecyclerView, favoriteImageList: List<ImageDocument>?) {
    (recyclerView.adapter as FavoriteListAdapter?)?.let { adapter ->
        val newList = if(favoriteImageList == null || favoriteImageList.isEmpty()) {
            null
        } else {
            ArrayList(favoriteImageList)
        }

        adapter.submitList(newList)

        if(newList == null) {
            adapter.notifyDataSetChanged()
        }
    }
}