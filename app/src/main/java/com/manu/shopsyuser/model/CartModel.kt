package com.manu.shopsyuser.model

data class CartModel(
    val productId: String = "",
    val productTitle: String = "",
    val size: String = "",
    val color: String = "",
    val productImg: String = "",
    val productMrp: String = "",
    val productSp: String = "",
)