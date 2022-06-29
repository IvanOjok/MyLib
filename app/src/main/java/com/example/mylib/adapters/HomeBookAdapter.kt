package com.example.mylib.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.ui.library.BookLessonsFragment

class HomeBookAdapter(r: Context, options: ArrayList<String>) : RecyclerView.Adapter<HomeBookAdapter.HomeVHolder>() {
    var y = options
    var c = r

    class HomeVHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle = itemView.findViewById<TextView>(R.id.lessons)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeVHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.home_book_list, parent, false)
        return HomeVHolder(inflate)
    }

    override fun onBindViewHolder(holder: HomeVHolder, position: Int) {
        holder.bookTitle.text = y[position]

//        holder.itemView.setOnClickListener {
//            val x = c as HomeActivity
//            val cart = BookLessonsFragment()
//            val bundle = Bundle()
//            bundle.putString("bookTitle", y[position].bookTitle)
//            bundle.putString("topicTitle", y[position].topicTitle)
//            bundle.putString("termTitle", y[position].termTitle)
//            bundle.putString("bookClass", y[position].bookClass)
//
//            cart.arguments = bundle
//            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, cart).addToBackStack(null).commit()
//
//        }
    }



    override fun getItemCount(): Int {
        return y.size
    }
}

