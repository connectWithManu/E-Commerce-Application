package com.manu.shopsyuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manu.shopsyuser.R
import com.manu.shopsyuser.databinding.ItemSizeBinding
import com.manu.shopsyuser.model.SizeModel

class SizeAdapter(
    val context: Context,
    private val onClick: OnSizeClicked
):ListAdapter<SizeModel, SizeAdapter.SizeVH>(DiffUtils) {
    inner class SizeVH(val binding: ItemSizeBinding): RecyclerView.ViewHolder(binding.root)

    private var selectedPosition = RecyclerView.NO_POSITION

    object DiffUtils: DiffUtil.ItemCallback<SizeModel>() {
        override fun areItemsTheSame(oldItem: SizeModel, newItem: SizeModel): Boolean {
            return oldItem.size == newItem.size
        }

        override fun areContentsTheSame(oldItem: SizeModel, newItem: SizeModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeVH {
        val binding = ItemSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeVH(binding)
    }

    override fun onBindViewHolder(holder: SizeVH, position: Int) {
        val item = getItem(position)
        holder.binding.sizeText.text = item.size.uppercase()



        if (position == selectedPosition) {
            holder.binding.sizeText.setTextColor(ContextCompat.getColor(context, R.color.c10))
            holder.binding.sizeText.setBackgroundResource(R.drawable.shape_gray_circle_color)
        } else {
            holder.binding.sizeText.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.binding.sizeText.setBackgroundResource(R.drawable.shape_gray)
        }

        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(selectedPosition)
            notifyItemChanged(previousSelectedPosition)

            onClick.onItemSizeClicked(item.size)

        }
    }

    interface OnSizeClicked {
        fun onItemSizeClicked(size: String)
    }
}