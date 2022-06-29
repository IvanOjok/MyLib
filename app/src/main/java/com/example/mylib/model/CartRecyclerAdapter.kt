package com.example.mylib.model

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.ui.store.StoreFragment

class CartRecyclerAdapter(val r: Context, val phone: String, var list: ArrayList<Cart>, val viewModel: CartViewModel) :
    RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>() {

    val c = r
    val p = phone
    var sum = 0
    var cost = 0
    // In this function we will add our groceryadapter.xml to kotlin class
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_list, parent, false)
        return CartViewHolder(view)
    }

    // This function is used to return total number of size of list.
    override fun getItemCount(): Int {
        return list.size
    }

    // In onBindViewHolder we will bind our itemViews with adapter
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        var currentPosition = list[position]

        val bookTitle = holder.itemView.findViewById<TextView>(R.id.title)

        val bookTerm = holder.itemView.findViewById<TextView>(R.id.term)

        val bookPrice = holder.itemView.findViewById<TextView>(R.id.price)

        val image = holder.itemView.findViewById<ImageView>(R.id.image)

        val remove = holder.itemView.findViewById<ImageView>(R.id.remove)

        bookTitle.text =  currentPosition.title
        bookTerm.text = currentPosition.term
        bookPrice.text = currentPosition.price
        Glide.with(c).load(currentPosition.url).into(image)


        remove.setOnClickListener {
            val p = currentPosition.price!!.toInt()
            sum -= p
            Log.d("sum", sum.toString())
            cost = sum
            Log.d("Delete", currentPosition.titleTerm!!)
            viewModel.delete(currentPosition, phone)

            //notifyDataSetChanged()
            list.removeAt(position)
            notifyItemRemoved(position)


        }

        // To get total cost
       // if(position == list.size -1){

            for (i in 0 until list.size){
                Log.d("list", list[i].price!!)
               val p = list[i].price!!.toInt()
               sum += p
                Log.d("sum", sum.toString())

            }
            cost = sum
       // }

    }
    // Inner class for viewHolder
    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    fun total(x:Int): Int{
        return x
    }

}
