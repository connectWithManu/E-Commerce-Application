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
import com.manu.shopsyuser.activities.DetailsActivity
import com.manu.shopsyuser.databinding.ItemCartBinding
import com.manu.shopsyuser.model.CartModel
import com.manu.shopsyuser.utils.Objects

class CartAdapter(
    val context: Context
):
ListAdapter<CartModel, CartAdapter.CartVH>(DiffUtils){
    inner class CartVH(val binding: ItemCartBinding): RecyclerView.ViewHolder(binding.root)

    object DiffUtils: DiffUtil.ItemCallback<CartModel>() {
        override fun areItemsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartModel, newItem: CartModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartVH {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartVH(binding)
    }

    override fun onBindViewHolder(holder: CartVH, position: Int) {
        val item = getItem(position)
        holder.binding.prodImg.load(item.productImg) {
            placeholder(R.drawable.placeholder)
        }
        val sp = "₹" + item.productSp
        holder.binding.prodSp.text = sp
        val mrp = "₹" + item.productMrp
        holder.binding.prodMrp.text = mrp
        holder.binding.prodTitle.text = item.productTitle
        val color = "color: " + item.color
        holder.binding.tvColor.text = color
        val size = "size: " + item.size.uppercase()
        holder.binding.tvSize.text = size

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(Objects.PRODUCT_ID, item.productId)
            context.startActivity(intent)
        }
    }
}