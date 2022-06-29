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
import com.example.mylib.model.Library
import com.example.mylib.ui.library.BookTermFragment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class LibraryBookAdapter(r: Context, options: List<Library>) : RecyclerView.Adapter<LibraryBookAdapter.LibraryBookViewHolder>(){
    var y = options
    var c = r

    class LibraryBookViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onBind(c: Context, title:String, subject:String, price:String, image:String){

            val bookTitle = itemView.findViewById<TextView>(R.id.bookTitle)
            bookTitle.text = title

            val bookSubject = itemView.findViewById<TextView>(R.id.bookSubject)
            bookSubject.text = subject

            val bookPrice = itemView.findViewById<TextView>(R.id.bookPrice)
            bookPrice.text = price

            val bookImage = itemView.findViewById<ImageView>(R.id.imageView4)
            Glide.with(c).load(image).into(bookImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryBookViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false)
        return LibraryBookViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: LibraryBookViewHolder, position: Int) {
        val start = y[position].startDate
        val end = y[position].endDate
        val sDf = SimpleDateFormat("dd-MMM-yyyy")
        val startDate = Calendar.getInstance().time
        val d1 = sDf.parse(start!!)
        val d2 = sDf.parse(end!!)
        val diff = d2.time - startDate.time
        val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

        holder.onBind(c, y[position].title!!, "${days.toString()} days", "0% complete", y[position].url!!)
        holder.itemView.setOnClickListener {

            val x = c as HomeActivity
            val purchases = BookTermFragment()
            val bundle = Bundle()
            bundle.putString("id", y[position].id)
            bundle.putString("title", y[position].title)
            bundle.putString("term", y[position].term)
            bundle.putString("price", y[position].price)
            bundle.putString("url", y[position].url)
            bundle.putString("titleTerm", y[position].titleTerm)
            bundle.putString("bookClass", y[position].bookClass)

            purchases.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()

            }
        }

    override fun getItemCount(): Int {
        return  y.size
    }

}