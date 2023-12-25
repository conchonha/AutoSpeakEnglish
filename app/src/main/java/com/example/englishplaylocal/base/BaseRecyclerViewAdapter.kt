package com.example.englishplaylocal.base

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewAdapter<T : Any, VB : ViewBinding>():
    RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VB>>() {
    protected var items = AsyncListDiffer(this,DiffCallback<T>())

    open fun updateItems(items: List<T>) {
        this.items.submitList(items)
    }

    @get:LayoutRes
    protected abstract val layoutId: Int

    override fun getItemCount() = items.currentList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BaseViewHolder<VB>(
        DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)
    )

    open fun getListItems() = items.currentList

    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)
}

class DiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.hashCode() == newItem.hashCode()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}