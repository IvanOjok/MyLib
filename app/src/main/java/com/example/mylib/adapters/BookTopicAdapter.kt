package com.example.mylib.adapters

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
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Topic
import com.example.mylib.ui.library.BookLessonsFragment

class BookTopicAdapter(r: Context, options: ArrayList<Topic>) : RecyclerView.Adapter<BookTopicAdapter.BookTopicHolder>() {
    var y = options
    var c = r

    class BookTopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val remove = itemView.findViewById<ImageView>(R.id.remove)

        fun onBind(c: Context, title:String, term: String, price:String, url: String) {

            val bookTitle = itemView.findViewById<TextView>(R.id.title)
            val complete = itemView.findViewById<TextView>(R.id.complete)

            val image = itemView.findViewById<ImageView>(R.id.image)

            bookTitle.text =  title
            complete.text = "0% complete"

            Glide.with(c).load(url).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookTopicHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.book_topic_list, parent, false)
        return BookTopicHolder(inflate)
    }

    override fun onBindViewHolder(holder: BookTopicHolder, position: Int) {
        holder.onBind(c, y[position].topicTitle!!, y[position].topicTheme!!, y[position].bookTitle!!, y[position].topicIcon!!)

//        holder.itemView.setOnClickListener {
//            val intent = Intent(c, LessonsActivity::class.java)
//            intent.putExtra("bookTitle", y[position].bookTitle)
//            intent.putExtra("topicTitle", y[position].topicTitle)
//            intent.putExtra("termTitle", y[position].termTitle)
//            intent.putExtra("bookClass", y[position].bookClass)
//            c.startActivity(intent)
//        }

        holder.itemView.setOnClickListener {
            val x = c as HomeActivity
            val cart = BookLessonsFragment()
            val bundle = Bundle()
           bundle.putString("bookTitle", y[position].bookTitle)
           bundle.putString("topicTitle", y[position].topicTitle)
           bundle.putString("termTitle", y[position].termTitle)
           bundle.putString("bookClass", y[position].bookClass)

            cart.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, cart).addToBackStack(null).commit()

        }
//        holder.itemView.setOnClickListener {
//            if (y[position].gender != "M" && y[position].gender != "F"){
//                c.startActivity(Intent(c, AddProfile::class.java))
//            }
//            else{
//                val intent = Intent(c, PurchaseActivity::class.java)
//                intent.putExtra("name", y[position].bookTitle)
//                c.startActivity(intent)


//            }
        // }
    }

    fun removeItem(position: Int){
        y.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, y.size)


    }

    override fun getItemCount(): Int {
        return y.size
    }
}

