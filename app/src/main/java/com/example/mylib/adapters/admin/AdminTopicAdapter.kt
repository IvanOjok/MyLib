package com.example.mylib.adapters.admin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.model.Topic
import com.example.mylib.ui.admin.AdminLessons

class AdminTopicAdapter(r: Context, options: ArrayList<Topic>) : RecyclerView.Adapter<AdminTopicAdapter.AdminTopicHolder>() {
    var y = options
    var c = r

    class AdminTopicHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val cart = itemView.findViewById<Button>(R.id.addToCart)
        val remove = itemView.findViewById<ImageView>(R.id.remove)

        fun onBind(c: Context, title:String, term: String, price:String, url: String) {

            val bookTitle = itemView.findViewById<TextView>(R.id.title)

//            val bookTerm = itemView.findViewById<TextView>(R.id.term)
//
//            val bookPrice = itemView.findViewById<TextView>(R.id.price)

            val image = itemView.findViewById<ImageView>(R.id.image)

            bookTitle.text =  title

//            bookTerm.text = term
//            bookPrice.text = price
            Glide.with(c).load(url).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminTopicHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.book_topic_list, parent, false)
        return AdminTopicHolder(inflate)
    }

    override fun onBindViewHolder(holder: AdminTopicHolder, position: Int) {
        holder.onBind(c, y[position].topicTitle!!, y[position].topicTheme!!, y[position].bookTitle!!, y[position].topicIcon!!)

        holder.itemView.setOnClickListener {
                val intent = Intent(c, AdminLessons::class.java)
                intent.putExtra("bookTitle", y[position].bookTitle)
            intent.putExtra("topicTitle", y[position].topicTitle)
            intent.putExtra("termTitle", y[position].termTitle)
            intent.putExtra("bookClass", y[position].bookClass)

                c.startActivity(intent)


         }
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

