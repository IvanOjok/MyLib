package com.example.mylib.adapters.admin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Book
import com.example.mylib.ui.admin.AdminBooks

class AdminClassAdapter(): RecyclerView.Adapter<AdminClassAdapter.AdminClassViewHolder>(){
    var y = ArrayList<String>()
    var c: Context?= null
    var book = ArrayList<Book>()

    constructor(r: Context, options: ArrayList<String>) : this() {
        this.c = r
        this.y = options
    }

    constructor(r: Context, options: ArrayList<String>, details: ArrayList<Book>) : this() {
        this.c = r
        this.y = options
        this.book = details
    }

    var viewPool = RecyclerView.RecycledViewPool()

    class AdminClassViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        //fun onBind(bookClass:String, name:String){

        val name = itemView.findViewById<TextView>(R.id.text)
        //n.text = name

        //val recycler = itemView.findViewById<RecyclerView>(R.id.books)

        // }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminClassViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return AdminClassViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: AdminClassViewHolder, position: Int) {
        // holder.onBind(y[position].gender, y[position].name)

        holder.name.text = y[position]
        holder.name.setOnClickListener {
            val intent = Intent(c, AdminBooks::class.java)
            intent.putExtra("class", y[position])
            c!!.startActivity(intent)
        }


  //      val layoutManager = LinearLayoutManager(holder.recycler.context, LinearLayoutManager.HORIZONTAL, false)
//        //layoutManager.initialPrefetchItemCount = book[position].bookDetail!!.size
//        val adapter = StoreBookAdapter(c!!, book)
//        Log.d("book", "$book")
//        holder.recycler.adapter = adapter
//        holder.recycler.layoutManager = layoutManager
//        holder.recycler.setRecycledViewPool(viewPool)
//
//        holder.itemView.setOnClickListener {
//
//        }

    }

    override fun getItemCount(): Int {
        return  y.size
    }

}