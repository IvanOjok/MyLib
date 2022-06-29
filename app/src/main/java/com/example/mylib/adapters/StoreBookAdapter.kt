package com.example.mylib.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.Home
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Book
import com.example.mylib.ui.store.PurchaseActivity
import com.example.mylib.ui.store.PurchasesFragment

class StoreBookAdapter(r: Context, options: List<Book>) : RecyclerView.Adapter<StoreBookAdapter.StoreBookViewHolder>(){
    var y = options
    var c = r

    class StoreBookViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreBookViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_list, parent, false)
        return StoreBookViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: StoreBookViewHolder, position: Int) {
        holder.onBind(c, y[position].bookTitle!!, y[position].bookSubject!!, y[position].bookPrice!!, y[position].bookUrl!!)
        holder.itemView.setOnClickListener {
//            if (y[position].gender != "M" && y[position].gender != "F"){
//                c.startActivity(Intent(c, AddProfile::class.java))
//            }
//            else{
//                val intent = Intent(c, PurchaseActivity::class.java)
//                intent.putExtra("name", y[position].bookTitle)
//                c.startActivity(intent)

            val x = c as HomeActivity
            val purchases = PurchasesFragment()
            val bundle = Bundle()
            bundle.putString("title", y[position].bookTitle)
            bundle.putString("description", y[position].bookDescription)
            bundle.putString("price", y[position].bookPrice)
            bundle.putString("url", y[position].bookUrl)
            bundle.putString("color", y[position].bookColor)
            bundle.putString("lessons", y[position].lessons)
            bundle.putString("topics", y[position].topics)
            bundle.putString("class", y[position].bookClass)

            purchases.arguments = bundle

            x.supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()

//            }
        }
    }

    override fun getItemCount(): Int {
        return  y.size
    }

}