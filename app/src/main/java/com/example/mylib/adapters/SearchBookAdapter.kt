package com.example.mylib.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.model.Book

class SearchBookAdapter(r: Context, options: List<Book>) : RecyclerView.Adapter<SearchBookAdapter.SearchBookVH>(){
    var y = options
    var c = r

    class SearchBookVH( itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun onBind(c: Context, title:String, subject:String, price:String, image:String){

            val bookTitle = itemView.findViewById<TextView>(R.id.bookTitle)
            bookTitle.text = title

            val bookSubject = itemView.findViewById<TextView>(R.id.bookSubject)
            bookSubject.visibility = View.INVISIBLE

            val bookPrice = itemView.findViewById<TextView>(R.id.bookPrice)
            bookPrice.visibility = View.INVISIBLE

            val bookImage = itemView.findViewById<ImageView>(R.id.imageView4)
            Glide.with(c).load(image).into(bookImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBookVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false)
        return SearchBookVH(inflate)
    }

    override fun onBindViewHolder(holder: SearchBookVH, position: Int) {
        holder.onBind(c, y[position].bookTitle!!, y[position].bookSubject!!, y[position].bookPrice!!, y[position].bookUrl!!)
        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return  y.size
    }

}