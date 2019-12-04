package com.ch.yoon.imagesearch.presentation.search.imagesearch

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.databinding.ItemImageListBinding
import com.ch.yoon.imagesearch.databinding.ItemRetryFooterBinding
import com.ch.yoon.imagesearch.presentation.base.BaseViewHolder
import com.ch.yoon.imagesearch.extension.cancelImageLoad

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
class ImageSearchResultsAdapter(
    private val viewModel: ImageSearchViewModel
) : ListAdapter<ImageDocument, RecyclerView.ViewHolder>(DiffCallback()) {

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
        viewModel: ImageSearchViewModel,
        @LayoutRes layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemImageListBinding>(layoutResId, parent) {

        init {
            binding.viewModel = viewModel
        }

        fun setItem(imageDocument: ImageDocument) {
            binding.imageDocument = imageDocument
            binding.executePendingBindings()
        }

        fun clear() {
            binding.imageView.cancelImageLoad()
        }
    }

    class RetryFooterViewHolder(
        viewModel: ImageSearchViewModel,
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

    class DiffCallback : DiffUtil.ItemCallback<ImageDocument>() {
        override fun areItemsTheSame(oldItem: ImageDocument, newItem: ImageDocument): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageDocument, newItem: ImageDocument): Boolean {
            return oldItem == newItem
        }
    }
}