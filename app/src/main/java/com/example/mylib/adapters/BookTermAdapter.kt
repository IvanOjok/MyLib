package com.example.mylib.adapters

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Term
import com.example.mylib.ui.library.BookTopicFragment

class BookTermAdapter(r: Context, options: ArrayList<Term>, color:String) : RecyclerView.Adapter<BookTermAdapter.BookTermHolder>() {
    var y = options
    var c = r
    val colour = color

    class BookTermHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val card = itemView.findViewById<CardView>(R.id.card)


        fun onBind(c: Context, term:String, topic: String, lessons:String, price: String, url:String) {

            val termTitle = itemView.findViewById<TextView>(R.id.term)
            termTitle.text =  term

            val topics = itemView.findViewById<TextView>(R.id.topics)
            topics.text = "Topics: $topic"

            val lesson = itemView.findViewById<TextView>(R.id.lessons)
            lesson.text = "Lessons: $lessons"


            val image = itemView.findViewById<ImageView>(R.id.icon)
            Glide.with(c).load(url).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookTermHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.book_term_list, parent, false)
        return BookTermHolder(inflate)
    }

    override fun onBindViewHolder(holder: BookTermHolder, position: Int) {
        holder.onBind(c, y[position].termTitle!!, y[position].topics!!, y[position].lessons!!, y[position].termPrice!!, y[position].termIcon!!)

        holder.card.setCardBackgroundColor(Color.parseColor(colour))
        holder.itemView.setOnClickListener {
            val x = c as HomeActivity
            val cart = BookTopicFragment()
            val bundle = Bundle()
            bundle.putString("title", y[position].bookTitle)
            bundle.putString("term", y[position].termTitle)
            bundle.putString("price", y[position].termPrice)
            bundle.putString("url", y[position].bookUrl)
            bundle.putString("bClass", y[position].bookClass)

//            bundle.putString("color", y[position].bookColor)
//            bundle.putString("lessons", y[position].lessons)
//            bundle.putString("topics", y[position].topics)

            cart.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, cart).addToBackStack("Store").commit()

        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}

