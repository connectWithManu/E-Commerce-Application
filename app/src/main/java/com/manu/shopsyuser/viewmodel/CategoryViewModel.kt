package com.manu.shopsyuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.manu.shopsyuser.model.AddProductModel
import com.manu.shopsyuser.model.CategoryModel
import com.manu.shopsyuser.utils.Objects

class CategoryViewModel(application: Application): AndroidViewModel(application) {

    private val fireStore = FirebaseFirestore.getInstance()

    private val dbRef = fireStore.collection(Objects.CATEGORY_REF)

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    private val _categoryList = MutableLiveData<List<CategoryModel>>()
    val categoryList: LiveData<List<CategoryModel>> = _categoryList

    private val _categoryWiseProducts = MutableLiveData<List<AddProductModel>>()
    val categoryWiseProducts: LiveData<List<AddProductModel>> = _categoryWiseProducts

    fun getCategories() {
        dbRef.orderBy("categoryName", Query.Direction.ASCENDING).get().addOnSuccessListener { categorySnapshot ->
            val categories = categorySnapshot.documents.mapNotNull {doc ->
                doc.toObject<CategoryModel>()
            }
            _categoryList.postValue(categories)

        }.addOnFailureListener {
            _isPosted.postValue(false)
        }

    }

    fun showCategoriesData(categoryName: String, subCategoryName: String) {
        fireStore.collection(Objects.PRODUCT_REF).whereEqualTo("productCategory", categoryName)
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


}