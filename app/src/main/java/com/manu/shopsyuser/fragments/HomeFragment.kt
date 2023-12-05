package com.manu.shopsyuser.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.R
import com.manu.shopsyuser.adapter.CategoryAdopter
import com.manu.shopsyuser.adapter.ProductAdapter
import com.manu.shopsyuser.databinding.FragmentHomeBinding
import com.manu.shopsyuser.factory.CategoryViewModelFactory
import com.manu.shopsyuser.factory.ProductViewModelFactory
import com.manu.shopsyuser.factory.SliderViewModelFactory
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.viewmodel.CategoryViewModel
import com.manu.shopsyuser.viewmodel.ProductViewModel
import com.manu.shopsyuser.viewmodel.SliderViewModel


class HomeFragment : Fragment(), ProductAdapter.OnWishListClick {
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var sliderViewModel: SliderViewModel
    private lateinit var sliderData: ArrayList<SlideModel>
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdopter: CategoryAdopter
    private lateinit var productViewModel: ProductViewModel
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sliderFactory = SliderViewModelFactory(requireActivity().application)
        sliderViewModel = ViewModelProvider(this, sliderFactory)[SliderViewModel::class.java]

        sliderViewModel.getSliders()
        sliderData = ArrayList()

        drawerToggle = ActionBarDrawerToggle(requireActivity(), binding.drawerLayout, R.string.open, R.string.close)
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(drawerToggle)


        binding.btMenu.setOnClickListener {
            binding.drawerLayout.open()
        }


        sliderViewModel.sliderList.observe(viewLifecycleOwner) {slidersList ->
            sliderData.clear()
            for(slider in slidersList) {
                sliderData.add(SlideModel(slider.sliderImg, ScaleTypes.CENTER_CROP))
            }
            binding.imageSlider.setImageList(sliderData)

        }




        val categoryFactory = CategoryViewModelFactory(requireActivity().application)
        categoryViewModel = ViewModelProvider(this, categoryFactory)[CategoryViewModel::class.java]
        categoryAdopter = CategoryAdopter(requireContext())
        binding.categoryRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.categoryRv.adapter = categoryAdopter

        categoryViewModel.getCategories()
        categoryViewModel.categoryList.observe(viewLifecycleOwner) {categoryList ->
            categoryAdopter.submitList(categoryList)
        }

        val productFactory = ProductViewModelFactory(requireActivity().application)
        productViewModel = ViewModelProvider(this, productFactory) [ProductViewModel::class.java]
        productAdapter = ProductAdapter(requireContext(), productViewModel,this)
        productViewModel.getProducts()
        binding.productRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.productRv.adapter = productAdapter

        productViewModel.productList.observe(viewLifecycleOwner) {productList ->
            productAdapter.submitList(productList)
        }


        categoryViewModel.isPosted.observe(viewLifecycleOwner) {
            if(it) {
                showToast("category fetched")
            } else {
                showToast("category not fetched")
            }
        }



    }



    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }


    override fun onItemClicked(productModel: AddProductModel, btWishList: ImageView) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        productViewModel.addToWishList(userId, productModel.productId, btWishList)


    }


}