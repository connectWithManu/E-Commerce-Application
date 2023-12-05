package com.manu.shopsyuser.model

data class AddProductModel(
    val productId: String = "",
    val coverImg: String = "",
    val productTitle: String = "",
    val productDescription: String = "",
    val productMrp: String = "",
    val productSp: String = "",
    val productRating: String = "",
    val productDiscount: String = "",
    val productTag: String = "",
    val productCategory: String = "",
    val productSubCategory: String = "",
    val productColorList: ArrayList<ColorModel> = ArrayList(),
    val productSize: ArrayList<SizeModel> = ArrayList(),
    val productImages: ArrayList<ProductImgUrlModel> = ArrayList(),
    val productAvailable: String = ""
)