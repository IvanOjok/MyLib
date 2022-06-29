package com.example.mylib.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CartViewModelFactory(private val repository: CartRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartViewModel(repository) as T
    }
}
