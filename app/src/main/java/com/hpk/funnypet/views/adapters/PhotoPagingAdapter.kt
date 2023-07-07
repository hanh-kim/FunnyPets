package com.hpk.funnypet.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.hpk.funnypet.databinding.ItemCellPhotoBinding
import com.hpk.funnypet.model.Photo
import onAvoidDoubleClick

class PhotoPagingAdapter :
    PagingDataAdapter<Photo, PhotoPagingAdapter.PhotoViewHolder>(diffCallBack) {

    companion object {
        const val LOADING_ITEM = 0
        const val PHOTO_ITEM = 1
    }

    var onItemClickListener: (Photo) -> Unit = {}

    inner class PhotoViewHolder(private val binding: ItemCellPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.onAvoidDoubleClick {
                getItem(bindingAdapterPosition)?.let {
                    onItemClickListener.invoke(it)
                }
            }
        }

        fun bindData() {
            getItem(bindingAdapterPosition)?.let {
                binding.model = it
            }
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount) PHOTO_ITEM else LOADING_ITEM
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bindData()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemCellPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

}

private val diffCallBack = object : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean = oldItem == newItem
}
