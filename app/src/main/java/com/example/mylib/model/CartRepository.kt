package com.example.mylib.model

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.ui.store.StoreFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

interface GetCartDao{
    suspend fun insert(phone: String, title:String, term:String, price:String, url:String, bClass:String){

        FirebaseDatabase.getInstance().getReference("/cart").child(phone).orderByChild("titleTerm").equalTo("$title$term").addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val db = FirebaseDatabase.getInstance().getReference("/cart")
                    val ref = db.child(phone).push()
                    val id = ref.key

                    val cartItem = Cart(id!!, title, term, price, url, "$title + Term 1, Term 2, Term 3", bClass)
                    ref.setValue(cartItem)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("error", error.message)
            }
        })
    }

    suspend fun delete(item:Cart, phone: String){
        val db = FirebaseDatabase.getInstance().getReference("/cart")
        db.child(phone).child(item.id!!).removeValue()

    }

    fun getCartItems(phone: String): LiveData<ArrayList<Cart>> {
        val p = ArrayList<Cart>()
        val x = MutableLiveData(ArrayList<Cart>())
        val ref = FirebaseDatabase.getInstance().getReference("/cart").child(phone)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    p.clear()
                    for (y in snapshot.children){
                        val item = y.getValue(Cart::class.java)
                        p.add(item!!)

                        x.value = p

                        Log.d("x", x.value.toString())

//                        b = item.price!!.toInt()
//                        sum += b

//                    Log.d("p", p.toString())
//                    Log.d("sum", sum.toString())
                    }

 //                   size = p.size
//
 //                   Log.d("sum", sum.toString())
                    Log.d("p", p.toString())
                   // Log.d("size", size.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(requireContext(), "An $error occured", Toast.LENGTH_LONG).show()
                    Log.d("size", error.toString())
                }

            })


       //  x.value = p

        x.postValue(p)

        Log.d("x", x.value.toString())

        return x
    }

    fun getCartPrice(phone: String): LiveData<HashMap<String, Int>>   {
        val p = ArrayList<Cart>()
        var b = 0
        var sum = 0
        var y = HashMap<String, Int>()
        val x = MutableLiveData(HashMap<String, Int>())
        val ref = FirebaseDatabase.getInstance().getReference("/cart").child(phone)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                   // p.clear()
                    for (t in snapshot.children){
                        Log.d("p", p.toString())
                        Log.d("snap", t.toString())
                        val item = t.getValue(Cart::class.java)
                        p.add(item!!)


                        Log.d("x", x.value.toString())

                        b = item.price!!.toInt()
                        sum += b

                    Log.d("p", p.toString())
                    Log.d("sum", sum.toString())
                    }

                                       val size = p.size

                                       Log.d("sum", sum.toString())
                    Log.d("p", p.toString())
                    // Log.d("size", size.toString())

                    y = hashMapOf(Pair("sum", sum), Pair("size", size))
                    x.value = y
                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(requireContext(), "An $error occured", Toast.LENGTH_LONG).show()
                    Log.d("size", error.toString())
                }

            })


        //  x.value = p

        x.postValue(y)

        Log.d("x", x.value.toString())

        return x
    }
}




class CartRepository(private val db: GetCartDao) {
    suspend fun insert(
        phone: String,
        title: String,
        term: String,
        price: String,
        url: String,
        bClass: String
    ) = db.insert(phone, title, term, price, url, bClass)
    suspend fun delete(item: Cart, phone: String) = db.delete(item, phone)

    fun allCartItems(phone: String) = db.getCartItems(phone)

    fun allCartPrice(phone: String) = db.getCartPrice(phone)
}