package com.example.mylib.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class  CartViewModel(private val repository: CartRepository) : ViewModel() {

    // In coroutines thread insert item in insert function.
    fun insert(phone:String, title:String, term:String, price:String, url:String, bClass:String) = GlobalScope.launch {
        repository.insert(phone, title, term, price, url, bClass)
    }

    // In coroutines thread delete item in delete function.
    fun delete(item: Cart, phone:String) = GlobalScope.launch {
        repository.delete(item, phone)
    }

    //Here we initialized allGroceryItems function with repository
    fun allCartItems(phone:String) = repository.allCartItems(phone)

    fun allcartPrice(phone: String) = repository.allCartPrice(phone)

}
