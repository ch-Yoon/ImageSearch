package com.ch.yoon.imagesearch.presentation.imagesearch.imagelist

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.util.common.loadImageWithCenterCrop
import java.util.ArrayList

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
@BindingAdapter("countOfItemInLine")
fun setSpanCount(recyclerView: RecyclerView, countOfItemInLine: Int) {
    val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager?
    gridLayoutManager?.spanCount = countOfItemInLine
}

@BindingAdapter("searchImageDocumentList")
fun setItems(recyclerView: RecyclerView, imageDocumentList: List<ImageDocument>?) {
    (recyclerView.adapter as ImageListAdapter?)?.let { adpater ->
        val newList = if(imageDocumentList == null || imageDocumentList.isEmpty()) {
            null
        } else {
            ArrayList(imageDocumentList)
        }

        adpater.submitList(newList)

        if(newList == null) {
            adpater.notifyDataSetChanged()
        }
    }
}

@BindingAdapter("imageSearchState")
fun setImageSearchState(recyclerView: RecyclerView, imageSearchState: ImageSearchState) {
    val adapter = recyclerView.adapter as ImageListAdapter?
    adapter?.run {
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

@BindingAdapter("loadImageWithCenterCrop")
fun loadImageWithCenterCrop(imageView: ImageView, imageUrl: String?) {
    loadImageWithCenterCrop(imageView, imageUrl)
}

@BindingAdapter("applySelectedState")
fun applySelectedState(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("srcAlpha")
fun setSrcAlpha(imageView: ImageView, alpha: Int) {
    imageView.drawable.alpha = alpha
}
