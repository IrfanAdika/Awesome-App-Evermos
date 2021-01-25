package com.irfan.awesomeapp.module.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.irfan.awesomeapp.R
import com.irfan.awesomeapp.data.model.Photos
import com.irfan.awesomeapp.databinding.ItemGridPhotoBinding
import com.irfan.awesomeapp.databinding.ItemListPhotoBinding
import com.vicpin.krealmextensions.queryAll
import io.realm.RealmList

class PhotoListAdapter(private val layoutManager: GridLayoutManager? = null, private val callback: PhotoItemCallback): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var photoList: ArrayList<Photos> = arrayListOf()

    enum class ViewType {
        LIST,
        GRID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            ViewType.LIST.ordinal -> {
                val binding = DataBindingUtil.inflate<ItemListPhotoBinding>(
                    LayoutInflater.from(parent.context), R.layout.item_list_photo,
                    parent, false)

                return ListPhotoViewHolder(binding)
            }
             else -> {
                 val binding = DataBindingUtil.inflate<ItemGridPhotoBinding>(
                     LayoutInflater.from(parent.context), R.layout.item_grid_photo,
                     parent, false)

                 return GridPhotoViewHolder(binding)
             }
        }

    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (layoutManager?.spanCount == 1) ViewType.LIST.ordinal
        else ViewType.GRID.ordinal
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ListPhotoViewHolder) {
            holder.binding.photo = photoList[position]
            holder.binding.listener = callback
            holder.binding.executePendingBindings()
        } else if (holder is GridPhotoViewHolder) {
            holder.binding.photo = photoList[position]
            holder.binding.listener = callback
            holder.binding.executePendingBindings()
        }
    }

    fun refreshAdapter(data: ArrayList<Photos>) {
        this.photoList = data
        notifyDataSetChanged()
    }

    class ListPhotoViewHolder(val binding: ItemListPhotoBinding) : RecyclerView.ViewHolder(binding.root)
    class GridPhotoViewHolder(val binding: ItemGridPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    interface PhotoItemCallback {
        fun onClickItem(data: Photos)
    }
}