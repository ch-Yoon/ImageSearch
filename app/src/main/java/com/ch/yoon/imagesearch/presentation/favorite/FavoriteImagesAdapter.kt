package com.ch.yoon.imagesearch.presentation.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ch.yoon.imagesearch.data.repository.image.model.ImageDocument
import com.ch.yoon.imagesearch.databinding.ItemFavoriteImageListBinding
import com.ch.yoon.imagesearch.extension.cancelImageLoad
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * Creator : ch-yoon
 * Date : 2019-11-13
 **/
class FavoriteImagesAdapter : ListAdapter<ImageDocument, FavoriteImagesAdapter.FavoriteListViewHolder>(DiffCallback()) {

    private val _itemClicks = PublishSubject.create<ImageDocument>()
    val itemClicks: Observable<ImageDocument> = _itemClicks

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FavoriteListViewHolder(ItemFavoriteImageListBinding.inflate(inflater, parent, false)).apply {
            binding.imageView.setOnClickListener {
                _itemClicks.onNext(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.setItem(getItem(position))
    }

    override fun onViewRecycled(holder: FavoriteListViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    class FavoriteListViewHolder(
        val binding: ItemFavoriteImageListBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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