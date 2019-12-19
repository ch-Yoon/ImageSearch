package com.ch.yoon.imagesearch.presentation.search.imagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.databinding.ItemImageListBinding
import com.ch.yoon.imagesearch.databinding.ItemRetryFooterBinding
import com.ch.yoon.imagesearch.extension.cancelImageLoad
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Creator : ch-yoon
 * Date : 2019-10-29
 **/
class ImageSearchResultsAdapter : ListAdapter<ImageDocument, RecyclerView.ViewHolder>(DiffCallback()) {

    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }

    private var retryFooterViewVisibility = false

    private val _itemClicks = PublishSubject.create<ImageDocument>()
    val itemClicks: Observable<ImageDocument> = _itemClicks

    private val _footerClicks = PublishSubject.create<Unit>()
    val footerClicks: Observable<Unit> = _footerClicks

    private val _bindPositions = PublishSubject.create<Int>()
    val bindPositions: Observable<Int> = _bindPositions

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_ITEM) {
            ImageSearchResultsViewHolder(ItemImageListBinding.inflate(inflater, parent, false)).apply {
                binding.imageView.setOnClickListener {
                    _itemClicks.onNext(getItem(adapterPosition))
                }
            }
        } else {
            RetryFooterViewHolder(ItemRetryFooterBinding.inflate(inflater, parent, false)).apply {
                binding.retryButton.setOnClickListener {
                    _footerClicks.onNext(Unit)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is ImageSearchResultsViewHolder -> {
                holder.setItem(getItem(position))
                _bindPositions.onNext(position)
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
            is ImageSearchResultsViewHolder -> {
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

    fun changeFooterViewVisibility(visibility: Boolean) {
        retryFooterViewVisibility = visibility
        notifyItemChanged(super.getItemCount())
    }

    class ImageSearchResultsViewHolder(
        val binding: ItemImageListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(imageDocument: ImageDocument) {
            binding.imageDocument = imageDocument
            binding.executePendingBindings()
        }

        fun clear() {
            binding.imageView.cancelImageLoad()
        }
    }

    class RetryFooterViewHolder(
        val binding: ItemRetryFooterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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