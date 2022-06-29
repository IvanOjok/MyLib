package com.example.mylib.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.ui.library.LessonsActivity
import com.example.mylib.ui.library.ReadingListLessonsFragment

class HomeReadListAdapter(): RecyclerView.Adapter<HomeReadListAdapter.ReadingListVH>(){
    var c: Context?= null
    var book = ArrayList<String>()
    var lessons:String ?= null

    constructor(r: Context, details: ArrayList<String>, lessons:String) : this() {
        this.c = r
        this.book = details
        this.lessons = lessons
    }

    class ReadingListVH( itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.lessons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingListVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.home_book_list, parent, false)
        return ReadingListVH(inflate)
    }

    override fun onBindViewHolder(holder: ReadingListVH, position: Int) {
        holder.title.text = book[position]
        holder.title.setTextColor(c!!.resources.getColor(R.color.white))


        holder.itemView.setOnClickListener {
//            if(lessons == "0"){
//                Toast.makeText(c, "no lessons", Toast.LENGTH_SHORT).show()
//            }
//            else {
                val x = c as HomeActivity
                val cart = ReadingListLessonsFragment()
                val bundle = Bundle()
                bundle.putString("title", book[position])


                cart.arguments = bundle
                x.supportFragmentManager.beginTransaction().replace(R.id.frag_container , cart).addToBackStack(null).commit()
      //      }
        }
    }

    override fun getItemCount(): Int {
        return  book.size
    }

}