package com.manu.shopsyuser.viewmodel

import android.app.Application
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.collect.Lists
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.manu.shopsyuser.R
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.model.CartModel
import com.manu.shopsyuser.utils.Objects

class ProductViewModel(application: Application) : AndroidViewModel(application) {


    private val db = FirebaseFirestore.getInstance()

    private val dbRef = db.collection(Objects.PRODUCT_REF)

    private val auth = FirebaseAuth.getInstance().currentUser!!.uid

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _productList = MutableLiveData<List<AddProductModel>>()
    val productList: LiveData<List<AddProductModel>> = _productList

    private val _cartList = MutableLiveData<List<CartModel>>()
    val cartList: LiveData<List<CartModel>> = _cartList

    private val _isInCartList = MutableLiveData<Boolean>()
    val isInCartList: LiveData<Boolean> = _isInCartList

    private val _wishList = MutableLiveData<List<AddProductModel>>()
    val wishList: LiveData<List<AddProductModel>> = _wishList

    private val _isInWishlist = MutableLiveData<Boolean>()
    val isInWishlist: LiveData<Boolean> = _isInWishlist

    private val _productDetails = MutableLiveData<AddProductModel>()
    val productDetails: LiveData<AddProductModel> = _productDetails

    private val _categoryWiseProducts = MutableLiveData<List<AddProductModel>>()
    val categoryWiseProducts: LiveData<List<AddProductModel>> = _categoryWiseProducts

    init {
        getProducts()
    }


    fun getProducts() {
        dbRef.orderBy("productSubCategory", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { productLists ->
                val products = productLists.documents.mapNotNull { doc ->
                    doc.toObject<AddProductModel>()
                }
                _productList.postValue(products)

            }.addOnFailureListener {
            _isPosted.postValue(false)
        }
    }

    fun getProductDetails(productId: String) {
        dbRef.document(productId).get().addOnSuccessListener { productDoc ->
            val product = productDoc.toObject<AddProductModel>()
            _productDetails.postValue(product!!)
        }.addOnFailureListener {
            _isPosted.postValue(false)
        }
    }

    fun showSimilarData(categoryName: String, subCategoryName: String) {
        dbRef.whereEqualTo("productCategory", categoryName)
            .whereEqualTo("productSubCategory", subCategoryName).get()
            .addOnSuccessListener {data ->
                val categoryWiseProducts = data.documents.mapNotNull {doc ->
                doc.toObject<AddProductModel>()
                }
                _categoryWiseProducts.postValue(categoryWiseProducts)
                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }
    }

    fun getWishList(userId: String) {
        val userDocRef = db.collection(Objects.FS_USERS).document(userId).collection(Objects.WISH_LIST)

        userDocRef.get().addOnSuccessListener { doc ->
            val wishList = doc.mapNotNull {
                it.toObject<AddProductModel>()
            }
            _wishList.postValue(wishList)
        }
    }

    fun getCartList(userId: String) {
        val userDocRef = db.collection(Objects.FS_USERS).document(userId).collection(Objects.CART_LIST)

        userDocRef.get().addOnSuccessListener { doc ->
            val cartList = doc.mapNotNull {
                it.toObject<CartModel>()
            }
            _cartList.postValue(cartList)
        }
    }

    fun addToWishList(userId: String, productId: String, wishListButton: ImageView) {
        val userDocRef = db.collection(Objects.FS_USERS).document(userId)
        val wishListRef = userDocRef.collection(Objects.WISH_LIST).document(productId)

        wishListRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {

                    wishListRef.delete()
                        .addOnSuccessListener {
                            _isInWishlist.postValue(false)
                            _isPosted.postValue(true)
                            updateWishListButton(false, wishListButton)
                        }
                        .addOnFailureListener {
                            _isPosted.postValue(false)
                        }
                } else {

                    val productRef = dbRef.document(productId)

                    productRef.get().addOnSuccessListener { productDoc ->
                        if (productDoc.exists()) {

                            val product = productDoc.toObject<AddProductModel>()
                            if (product != null) {
                                wishListRef.set(product)
                                    .addOnSuccessListener {
                                        _isInWishlist.postValue(true)
                                        _isPosted.postValue(true)
                                        updateWishListButton(true, wishListButton)
                                    }
                                    .addOnFailureListener {
                                        _isPosted.postValue(false)
                                    }
                            }
                        } else {

                            _isPosted.postValue(false)
                        }
                    }.addOnFailureListener {
                        _isPosted.postValue(false)
                    }
                }
            }
    }

    fun checkWishlistStatus(userId: String, productId: String, btWishList: ImageView) {
        val wishListRef = db.collection(Objects.FS_USERS)
            .document(userId)
            .collection(Objects.WISH_LIST)
            .document(productId)

        wishListRef.get().addOnSuccessListener { documentSnapshot ->
            updateWishListButton(documentSnapshot.exists(),btWishList )
            _isInWishlist.postValue(documentSnapshot.exists())

        }.addOnFailureListener {
            _isInWishlist.postValue(false)

        }
    }

    private fun updateWishListButton(inWishList: Boolean, wishListButton: ImageView) {
        if (inWishList) {
            wishListButton.setImageResource(R.drawable.ic_fav_filled_pink)
        } else {
            wishListButton.setImageResource(R.drawable.ic_fav_outline)
        }
    }

    fun addToCartList(userId: String, cartModel: CartModel, cartStatus: TextView) {
        val userDocRef = db.collection(Objects.FS_USERS).document(userId)
        val cartListRef = userDocRef.collection(Objects.CART_LIST).document(cartModel.productId)

        cartListRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    cartListRef.delete()
                        .addOnSuccessListener {
                            _isInCartList.postValue(false)
                            _isPosted.postValue(true)
                            updateCartListButton(false, cartStatus)
                            getCartList(auth)
                        }
                        .addOnFailureListener {
                            _isPosted.postValue(false)
                        }
                } else {

                    val productRef = dbRef.document(cartModel.productId)

                    productRef.get().addOnSuccessListener { productDoc ->
                        if (productDoc.exists()) {
                            val product = productDoc.toObject<CartModel>()
                            if (product != null) {
                                userDocRef.collection(Objects.CART_LIST).document(cartModel.productId)
                                    .set(cartModel)
                                    .addOnSuccessListener {
                                        _isInCartList.postValue(true)
                                        _isPosted.postValue(true)
                                        updateCartListButton(true, cartStatus)
                                        getCartList(auth)
                                    }
                                    .addOnFailureListener {
                                        _isPosted.postValue(false)
                                    }
                            }
                        } else {
                            _isPosted.postValue(false)
                        }
                    }.addOnFailureListener {
                        _isPosted.postValue(false)
                    }
                }
            }
    }

    fun checkCartListStatus(userId: String, productId: String, cartStatus: TextView) {
        val wishListRef = db.collection(Objects.FS_USERS)
            .document(userId)
            .collection(Objects.CART_LIST)
            .document(productId)

        wishListRef.get().addOnSuccessListener { documentSnapshot ->
            _isInCartList.postValue(documentSnapshot.exists())
            updateCartListButton(documentSnapshot.exists(),cartStatus )
        }.addOnFailureListener {
            _isInCartList.postValue(false)

        }
    }

    private fun updateCartListButton(inCartList: Boolean, cartStatus: TextView) {
        if (inCartList) {
            cartStatus.text = "Go to Cart"
        } else {
            cartStatus.text = "Add to Cart"
        }
    }



}