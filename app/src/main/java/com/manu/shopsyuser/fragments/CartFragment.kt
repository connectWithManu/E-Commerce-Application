package com.manu.shopsyuser.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.R
import com.manu.shopsyuser.adapter.CartAdapter
import com.manu.shopsyuser.databinding.FragmentCartBinding
import com.manu.shopsyuser.factory.ProductViewModelFactory
import com.manu.shopsyuser.viewmodel.ProductViewModel

class CartFragment : Fragment() {
    private val binding by lazy { FragmentCartBinding.inflate(layoutInflater) }
    private lateinit var productViewModel: ProductViewModel
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(requireContext())

        val productFactory = ProductViewModelFactory(requireActivity().application)
        productViewModel = ViewModelProvider(this, productFactory)[ProductViewModel::class.java]
        binding.cartRv.adapter = cartAdapter

        productViewModel.getCartList(FirebaseAuth.getInstance().currentUser!!.uid)

        productViewModel.cartList.observe(viewLifecycleOwner) {
            cartAdapter.submitList(it)
        }

    }

}