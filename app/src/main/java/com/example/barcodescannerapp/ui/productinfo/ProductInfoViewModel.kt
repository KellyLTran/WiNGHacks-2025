package com.example.barcodescannerapp.ui.productinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductInfoViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Product Info Fragment"
    }
    val text: LiveData<String> = _text
}