package com.manu.shopsyuser.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.R
import com.manu.shopsyuser.activities.DetailsActivity
import com.manu.shopsyuser.databinding.ItemProductBinding
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.utils.Objects
import com.manu.shopsyuser.viewmodel.ProductViewModel

class ProductAdapter(
    val context: Context,
    private val productViewModel: ProductViewModel,
    private val onClick: OnWishListClick
) :
    ListAdapter<AddProductModel, ProductAdapter.ProductVH>(DiffUtils) {
    inner class ProductVH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)


    object DiffUtils : DiffUtil.ItemCallback<AddProductModel>() {
        override fun areItemsTheSame(oldItem: AddProductModel, newItem: AddProductModel): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(
            oldItem: AddProductModel,
            newItem: AddProductModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductVH {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductVH(binding)
    }

    override fun onBindViewHolder(holder: ProductVH, position: Int) {
        val item = getItem(position)
        holder.binding.prodImg.load(item.coverImg) {
            placeholder(R.drawable.placeholder)
        }
        val sp = "₹" + item.productSp
        holder.binding.prodSp.text = sp
        val percentage = item.productDiscount + " % OFF"
        holder.binding.prodDiscount.text = percentage

        if(item.productTag.isEmpty()) {
            holder.binding.prodTag.visibility = View.GONE
        }else {
            holder.binding.prodTag.text = item.productTag
        }

        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        productViewModel.checkWishlistStatus(userId, item.productId, holder.binding.btWishList)

        holder.binding.prodRating.text = item.productRating
        holder.binding.prodTitle.text = item.productTitle
        val mrp = "₹" + item.productMrp

        holder.binding.prodMrp.text = mrp
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra(Objects.PRODUCT_ID, item.productId)
            context.startActivity(intent)
        }

        holder.binding.btWishList.setOnClickListener {
            onClick.onItemClicked(item, holder.binding.btWishList)
        }

    }

    interface OnWishListClick {
        fun onItemClicked(productModel: AddProductModel, btWishList: ImageView)
    }

}

