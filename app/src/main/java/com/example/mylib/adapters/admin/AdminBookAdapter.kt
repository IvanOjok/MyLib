package com.example.mylib.adapters.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.model.Book
import com.example.mylib.ui.admin.AdminTerms

class AdminBookAdapter(r: Context, options: List<Book>) : RecyclerView.Adapter<AdminBookAdapter.AdminBookViewHolder>(){
    var y = options
    var c = r

    class AdminBookViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminBookViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false)
        return AdminBookViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: AdminBookViewHolder, position: Int) {
        holder.onBind(c, y[position].bookTitle!!, y[position].bookSubject!!, y[position].bookPrice!!, y[position].bookUrl!!)
        holder.itemView.setOnClickListener {
//            if (y[position].gender != "M" && y[position].gender != "F"){
//                c.startActivity(Intent(c, AddProfile::class.java))
//            }
//            else{
                val intent = Intent(c, AdminTerms::class.java)
                intent.putExtra("bTitle", y[position].bookTitle)
                intent.putExtra("bUrl", y[position].bookUrl)
                intent.putExtra("bClass", y[position].bookClass)

                c.startActivity(intent)
//            }
        }
    }

    override fun getItemCount(): Int {
        return  y.size
    }

}