package com.manu.shopsyuser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.R
import com.manu.shopsyuser.adapter.ProductAdapter
import com.manu.shopsyuser.databinding.FragmentFavoriteBinding
import com.manu.shopsyuser.factory.ProductViewModelFactory
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.viewmodel.ProductViewModel


class FavoriteFragment : Fragment(), ProductAdapter.OnWishListClick {
    private val binding by lazy { FragmentFavoriteBinding.inflate(layoutInflater) }
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

        val productFactory = ProductViewModelFactory(requireActivity().application)
        productViewModel = ViewModelProvider(this, productFactory)[ProductViewModel::class.java]

        productAdapter = ProductAdapter(requireContext(), productViewModel, this)
        binding.wishListRv.layoutManager = GridLayoutManager(requireContext(), 2)

        productViewModel.getWishList(FirebaseAuth.getInstance().currentUser!!.uid)
        productViewModel.wishList.observe(viewLifecycleOwner) {
            productAdapter.submitList(it)
        }

        binding.wishListRv.adapter = productAdapter

    }

    override fun onItemClicked(productModel: AddProductModel, btWishList: ImageView) {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        productViewModel.addToWishList(userId, productModel.productId, btWishList)
    }
}