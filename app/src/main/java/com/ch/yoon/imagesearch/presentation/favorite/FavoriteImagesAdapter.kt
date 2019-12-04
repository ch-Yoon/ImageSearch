package com.ch.yoon.imagesearch.presentation.favorite

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.ch.yoon.imagesearch.R
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.databinding.ItemFavoriteImageListBinding
import com.ch.yoon.imagesearch.presentation.base.BaseViewHolder
import com.ch.yoon.imagesearch.extension.cancelImageLoad

/**
 * Creator : ch-yoon
 * Date : 2019-11-13
 **/
class FavoriteImagesAdapter(
    private val viewModel: FavoriteImagesViewModel
) : ListAdapter<ImageDocument, FavoriteImagesAdapter.FavoriteListViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        return FavoriteListViewHolder(viewModel, R.layout.item_favorite_image_list, parent)
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.setItem(getItem(position))
    }

    override fun onViewRecycled(holder: FavoriteListViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    class FavoriteListViewHolder(
        viewModel: FavoriteImagesViewModel,
        @LayoutRes layoutResId: Int,
        parent: ViewGroup
    ) : BaseViewHolder<ItemFavoriteImageListBinding>(layoutResId, parent) {

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

    class DiffCallback : DiffUtil.ItemCallback<ImageDocument>() {
        override fun areItemsTheSame(oldItem: ImageDocument, newItem: ImageDocument): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageDocument, newItem: ImageDocument): Boolean {
            return oldItem == newItem
        }
    }
}