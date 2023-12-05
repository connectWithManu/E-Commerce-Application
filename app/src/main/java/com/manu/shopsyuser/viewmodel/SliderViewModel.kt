package com.manu.shopsyuser.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.manu.shopsyuser.model.SliderModel
import com.manu.shopsyuser.utils.Objects

class SliderViewModel(application: Application): AndroidViewModel(application) {
    private val fireStore = FirebaseFirestore.getInstance()

    private val dbRef = fireStore.collection(Objects.SLIDER_REF)

    private val _isFetched = MutableLiveData<Boolean>()
    val isFetched: LiveData<Boolean> = _isFetched


    private val _sliderList = MutableLiveData<List<SliderModel>>()
    val sliderList: LiveData<List<SliderModel>> = _sliderList


    fun getSliders() {
        dbRef.get().addOnSuccessListener {
            val sliders = it.documents.mapNotNull { sliderList ->
                sliderList.toObject<SliderModel>()
            }
            _sliderList.postValue(sliders)
        }.addOnFailureListener {
            _isFetched.postValue(false)
        }
    }


}

