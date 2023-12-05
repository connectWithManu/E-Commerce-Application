package com.manu.shopsyuser.activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.adapter.ColorAdapter
import com.manu.shopsyuser.adapter.ProductAdapter
import com.manu.shopsyuser.adapter.SizeAdapter
import com.manu.shopsyuser.databinding.ActivityDetailsBinding
import com.manu.shopsyuser.factory.ProductViewModelFactory
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.model.CartModel
import com.manu.shopsyuser.utils.Objects
import com.manu.shopsyuser.viewmodel.ProductViewModel

class DetailsActivity : AppCompatActivity(), ProductAdapter.OnWishListClick, SizeAdapter.OnSizeClicked, ColorAdapter.OnColorClicked {
    private val binding by lazy { ActivityDetailsBinding.inflate(layoutInflater) }
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productId: String
    private lateinit var userId: String
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var colorAdapter: ColorAdapter
    private lateinit var productAdapter: ProductAdapter
    private var colorName: String = ""
    private var sizeName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productId = intent.getStringExtra(Objects.PRODUCT_ID)!!
        userId = FirebaseAuth.getInstance().currentUser!!.uid

        val productFactory = ProductViewModelFactory(application)
        productViewModel = ViewModelProvider(this, productFactory) [ProductViewModel::class.java]

        productViewModel.getProductDetails(productId)
        colorAdapter = ColorAdapter(this, this)
        sizeAdapter = SizeAdapter(this, this)
        productAdapter = ProductAdapter(this@DetailsActivity, productViewModel,this)
        binding.colorRv.adapter = colorAdapter
        binding.sizeRv.adapter = sizeAdapter

        binding.similarRv.layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        binding.similarRv.adapter = productAdapter


        productViewModel.checkCartListStatus(userId, productId, binding.tvCartStatus)

        productViewModel.productDetails.observe(this) {product ->
            binding.tvProdTitle.text = product.productTitle
            val dc = product.productDiscount + " % OFF"
            binding.tvDescription.text = product.productDescription
            binding.tvProdDiscount.text = dc
            val mrp = "₹" + product.productMrp
            binding.tvProdMrp.text = mrp
            val sp = "₹" + product.productSp
            binding.tvProdSp.text = sp

            colorAdapter.submitList(product.productColorList)
            sizeAdapter.submitList(product.productSize)

            val imgList = ArrayList<SlideModel>()
            for(img in product.productImages) {
                imgList.add(SlideModel(img.proImg, ScaleTypes.CENTER_CROP))
            }

            binding.imageSlider.setImageList(imgList)

            productViewModel.showSimilarData(product.productCategory, product.productSubCategory)

            productViewModel.productList.observe(this) {
                productAdapter.submitList(it)
            }

            binding.btAddCart.setOnClickListener {
                val intent = Intent(this, HomeMainActivity::class.java)
                intent.putExtra("cart", true)
                startActivity(intent)
            }

            binding.btCartLayout.setOnClickListener {
               if(binding.tvCartStatus.text == "Go to Cart") {
                val intent = Intent(this, HomeMainActivity::class.java)
                intent.putExtra("cart", true)
                startActivity(intent)
               } else if(colorName.isEmpty()) {
                   showToast("Please select Color")
               } else if(sizeName.isEmpty()) {
                   showToast("Please select Size")
               } else {
                val cartModel = CartModel(productId, product.productTitle, sizeName, colorName, product.coverImg, product.productMrp, product.productSp)
                productViewModel.addToCartList(userId, cartModel, binding.tvCartStatus)
            }
            }

        }


    }

    override fun onItemClicked(productModel: AddProductModel, btWishList: ImageView) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        productViewModel.addToWishList(userId, productModel.productId, btWishList)
    }

    override fun onItemColorClicked(color: String) {
        if(binding.tvCartStatus.text == "Go to Cart") {
            val cart = "Add to Cart"
            binding.tvCartStatus.text = cart
        }
        colorName = color
    }

    override fun onItemSizeClicked(size: String) {
        if(binding.tvCartStatus.text == "Go to Cart") {
            val cart = "Add to Cart"
            binding.tvCartStatus.text = cart
        }
        sizeName = size
    }

    private fun showToast(msg: String) {
        Toast.makeText(this@DetailsActivity, msg, Toast.LENGTH_SHORT).show()
    }
}