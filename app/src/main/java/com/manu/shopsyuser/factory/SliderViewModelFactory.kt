package com.manu.shopsyuser.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.manu.shopsyuser.viewmodel.SliderViewModel

@Suppress("UNCHECKED_CAST")
class SliderViewModelFactory(private val application: Application): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SliderViewModel::class.java)) {
            return SliderViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

