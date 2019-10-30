package com.ch.yoon.kakao.pay.imagesearch.ui.imagesearch.imagelist

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.kakao.pay.imagesearch.R
import com.ch.yoon.kakao.pay.imagesearch.data.source.remote.kakao.response.KakaoImageDocument
import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemImageListBinding
import com.ch.yoon.kakao.pay.imagesearch.databinding.ItemRetryFooterBinding
import com.ch.yoon.kakao.pay.imagesearch.ui.base.BaseViewHolder
import com.ch.yoon.kakao.pay.imagesearch.utils.cancelImageLoad

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
class ImageListAdapter(
    private val viewModel: ImageListViewModel
) : ListAdapter<KakaoImageDocument, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    private var retryFooterViewVisibility = false

    var onBindPosition: ((position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            ImageListViewHolder(viewModel, R.layout.item_image_list, parent)
        } else {
            RetryFooterViewHolder(viewModel, R.layout.item_retry_footer, parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ImageListViewHolder -> {
                holder.setItem(getItem(position))
                onBindPosition?.invoke(position)
            }
            is RetryFooterViewHolder -> {
                holder.setRetryVisibility(retryFooterViewVisibility)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isFooterViewPosition(position)) {
            TYPE_FOOTER
        } else {
            TYPE_ITEM
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        when(holder) {
            is ImageListViewHolder -> {
                holder.clear()
            }
        }

        super.onViewRecycled(holder)
    }

    override fun getItemCount(): Int {
        val itemCount = super.getItemCount()
        return if (itemCount == 0) {
            itemCount
        } else {
            itemCount + 1
        }
    }

    fun isFooterViewPosition(position: Int): Boolean {
        return position == super.getItemCount()
    }

    fun changeFooterViewVisibility(retryFooterViewVisibility: Boolean) {
        this.retryFooterViewVisibility = retryFooterViewVisibility
        notifyItemChanged(super.getItemCount())
    }

    class ImageListViewHolder(
        viewModel: ImageListViewModel,
        @LayoutRes layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemImageListBinding>(layoutResId, parent) {

        init {
            binding.viewModel = viewModel
        }

        fun setItem(kakaoImageDocument: KakaoImageDocument) {
            binding.imageDocument = kakaoImageDocument
            binding.executePendingBindings()
        }

        fun clear() {
            cancelImageLoad(binding.imageView)
        }
    }

    class RetryFooterViewHolder(
        viewModel: ImageListViewModel,
        @LayoutRes layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemRetryFooterBinding>(layoutResId, parent) {

        init {
            binding.viewModel = viewModel
        }

        fun setRetryVisibility(visible: Boolean) {
            binding.retryButtonVisibility = visible
            binding.executePendingBindings()
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<KakaoImageDocument>() {
        override fun areItemsTheSame(oldItem: KakaoImageDocument, newItem: KakaoImageDocument): Boolean {
            return oldItem.thumbnailUrl == newItem.thumbnailUrl
        }

        override fun areContentsTheSame(oldItem: KakaoImageDocument, newItem: KakaoImageDocument): Boolean {
            return oldItem == newItem
        }
    }
}