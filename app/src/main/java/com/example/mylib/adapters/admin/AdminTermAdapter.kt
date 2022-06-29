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
import com.example.mylib.model.Term
import com.example.mylib.ui.admin.AdminBookTopics

class AdminTermAdapter(r: Context, options: ArrayList<Term>) : RecyclerView.Adapter<AdminTermAdapter.AdminTermHolder>() {
    var y = options
    var c = r

    class AdminTermHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminTermHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.book_term_list, parent, false)
        return AdminTermHolder(inflate)
    }

    override fun onBindViewHolder(holder: AdminTermHolder, position: Int) {
        holder.onBind(c, y[position].termTitle!!, y[position].topics!!, y[position].lessons!!, y[position].termPrice!!, y[position].termIcon!!)

        holder.itemView.setOnClickListener {
            val intent = Intent(c, AdminBookTopics::class.java)
            intent.putExtra("title", y[position].bookTitle)
            intent.putExtra("term", y[position].termTitle)
            intent.putExtra("class", y[position].bookClass)
            c.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return y.size
    }
}

