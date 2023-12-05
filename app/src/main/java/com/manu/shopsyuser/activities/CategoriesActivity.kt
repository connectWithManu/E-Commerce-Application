package com.manu.shopsyuser.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.adapter.ProductAdapter
import com.manu.shopsyuser.databinding.ActivityCategoriesBinding
import com.manu.shopsyuser.factory.CategoryViewModelFactory
import com.manu.shopsyuser.factory.ProductViewModelFactory
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.utils.Objects
import com.manu.shopsyuser.viewmodel.CategoryViewModel
import com.manu.shopsyuser.viewmodel.ProductViewModel

class CategoriesActivity : AppCompatActivity(), ProductAdapter.OnWishListClick {
    private val binding by lazy { ActivityCategoriesBinding.inflate(layoutInflater) }
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra(Objects.CATEGORY_NAME)
        val subCategoryName = intent.getStringExtra(Objects.SUB_CATEGORY_NAME)


        val categoryFactory = CategoryViewModelFactory(application)
        val productFactory = ProductViewModelFactory(application)
        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]
        productViewModel= ViewModelProvider(this, productFactory)[ProductViewModel::class.java]
        productAdapter = ProductAdapter(this@CategoriesActivity, productViewModel, this)
        categoryViewModel.showCategoriesData(categoryName!!, subCategoryName!!)

        binding.productsRv.layoutManager = GridLayoutManager(this@CategoriesActivity, 2)
        binding.productsRv.adapter = productAdapter

        categoryViewModel.categoryWiseProducts.observe(this) {productList ->
            productAdapter.submitList(productList)
        }



    }

    override fun onItemClicked(productModel: AddProductModel, btWishList: ImageView) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        productViewModel.addToWishList(userId, productModel.productId, btWishList)
    }


}