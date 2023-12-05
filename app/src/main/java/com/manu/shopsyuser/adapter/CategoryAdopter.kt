package com.manu.shopsyuser.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.manu.shopsyuser.R
import com.manu.shopsyuser.activities.CategoriesActivity
import com.manu.shopsyuser.databinding.ItemCategoryBinding
import com.manu.shopsyuser.model.CategoryModel
import com.manu.shopsyuser.utils.Objects

class CategoryAdopter(val context: Context):ListAdapter<CategoryModel, CategoryAdopter.CategoryVH>(DiffUtils) {
    inner class CategoryVH(val binding: ItemCategoryBinding):RecyclerView.ViewHolder(binding.root)

    object DiffUtils: DiffUtil.ItemCallback<CategoryModel>() {
        override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem.categoryId == newItem.categoryId
        }

        override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryVH {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return CategoryVH(binding)
    }

    override fun onBindViewHolder(holder: CategoryVH, position: Int) {
        val item = getItem(position)
        holder.binding.tvCategroyTitle.text = item.categoryName
        holder.binding.ivCategoryImg.load(item.categoryImage){
            placeholder(R.drawable.placeholder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoriesActivity::class.java)
            intent.putExtra(Objects.CATEGORY_NAME, item.categoryName)
            intent.putExtra(Objects.SUB_CATEGORY_NAME, item.subCategory)
            context.startActivity(intent)
        }
    }


}