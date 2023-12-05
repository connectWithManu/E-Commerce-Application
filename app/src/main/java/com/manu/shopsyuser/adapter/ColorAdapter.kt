package com.manu.shopsyuser.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.manu.shopsyuser.R
import com.manu.shopsyuser.databinding.ItemColorBinding
import com.manu.shopsyuser.databinding.ItemSizeBinding
import com.manu.shopsyuser.model.ColorModel

class ColorAdapter(
    val context: Context,
    private val onClick: OnColorClicked
):ListAdapter<ColorModel, ColorAdapter.SizeVH>(DiffUtils) {
    inner class SizeVH(val binding: ItemColorBinding): RecyclerView.ViewHolder(binding.root)

    private var selectedPosition = RecyclerView.NO_POSITION

    object DiffUtils: DiffUtil.ItemCallback<ColorModel>() {
        override fun areItemsTheSame(oldItem: ColorModel, newItem: ColorModel): Boolean {
            return oldItem.color == newItem.color
        }

        override fun areContentsTheSame(oldItem: ColorModel, newItem: ColorModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeVH {
        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizeVH(binding)
    }

    override fun onBindViewHolder(holder: SizeVH, position: Int) {
        val item = getItem(position)

        if (position == selectedPosition) {
            holder.binding.tvColorName.setTextColor(ContextCompat.getColor(context, R.color.c10))

        } else {
            holder.binding.tvColorName.setTextColor(ContextCompat.getColor(context, R.color.black))

        }

        holder.binding.imageView3.setColorFilter(Color.parseColor(item.color))
        holder.binding.tvColorName.text = item.colorName
        holder.itemView.setOnClickListener {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position

            notifyItemChanged(selectedPosition)
            notifyItemChanged(previousSelectedPosition)

            onClick.onItemColorClicked(item.colorName)
        }
    }

    interface OnColorClicked {
        fun onItemColorClicked(color: String)
    }
}