package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.data.remote.kakao.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.utils.loadImageWithCenterCrop
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

@BindingAdapter("searchImageInfoList")
fun setItems(recyclerView: RecyclerView, imageDocumentList: List<ImageDocument>?) {
    val adapter = recyclerView.adapter as ImageListAdapter?
    adapter?.run {
        submitList(if(imageDocumentList == null) null else ArrayList(imageDocumentList))
        if(imageDocumentList == null || imageDocumentList.isEmpty()) {
            notifyDataSetChanged()
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
