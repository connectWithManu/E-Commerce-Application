package com.manu.shopsyuser.utils

object Objects {
    //firebase storage
    const val CATEGORY_IMG = "Categories/"
    const val SLIDER_IMG = "Sliders/"
    const val COVER_IMG = "CoverImg/"
    const val PRODUCT_IMAGES = "ProductImages/"

    //firebase fireStore
    const val CATEGORY_REF = "Categories"
    const val SLIDER_REF = "Sliders"
    const val PRODUCT_REF = "Products"
    const val FS_USERS = "Users"
    const val WISH_LIST = "WishList"
    const val CART_LIST = "CartList"

    //intents
    const val CATEGORY_NAME = "categoryName"
    const val SUB_CATEGORY_NAME = "subCategoryName"
    const val PRODUCT_ID = "productId"

    //list
    val subCategoryList = listOf("Men", "Women", "Kids")
    val availableList = listOf("Available", "Out of Stock", "Limited Stock", "Discontinued")
}