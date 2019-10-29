package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.data.model.imagesearch.response.ImageDocument
import com.ch.yoon.kakao.pay.imagesearch.utils.CollectionUtil
import com.ch.yoon.kakao.pay.imagesearch.utils.GlideUtil
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
        if(CollectionUtil.isEmpty(imageDocumentList)) {
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
    GlideUtil.loadWithCenterCrop(imageView, imageUrl)
}

@BindingAdapter("inputtedCountOfItemInLine", "expectedCountOfItemInLine")
fun applySelectedState(view: View, inputtedCountOfItemInLine: Int, expectedCountOfItemInLine: Int) {
    view.isSelected = inputtedCountOfItemInLine == expectedCountOfItemInLine
}

@BindingAdapter("applySelectedState")
fun applySelectedState(view: View, isSelected: Boolean) {
    view.isSelected = isSelected
}
