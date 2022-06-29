package com.example.mylib.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StoreClassAdapter() : RecyclerView.Adapter<StoreClassAdapter.StoreClassViewHolder>(){
    var y = ArrayList<String>()
    var c: Context ?= null
    var book = ArrayList<Book>()
   // lateinit var recyclerViewReadyCallBack:RecyclerViewReadyCallBack
   // lateinit var ada: StoreBookAdapter

    constructor(r: Context, options: ArrayList<String>) : this() {
        this.c = r
        this.y = options
    }

    constructor(r: Context, options: ArrayList<String>, details: ArrayList<Book>) : this() {
        this.c = r
        this.y = options
        this.book = details
        Log.d("details", details.toString())
     //   ada = StoreBookAdapter(c!!, details)
       // Log.d("itemsize", ada.itemCount.toString())
    }

    var viewPool = RecyclerView.RecycledViewPool()

   class StoreClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val name = itemView.findViewById<TextView>(R.id.className)
            val recycler = itemView.findViewById<RecyclerView>(R.id.books)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreClassViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_layout_list, parent, false)
        return StoreClassViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: StoreClassViewHolder, position: Int) {

        val currentPos = y[position]
              holder.name.text = currentPos

            Log.d("book", "$book")

        FirebaseDatabase.getInstance().getReference("/book").child(currentPos)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("data", snapshot.toString())
                    val x = ArrayList<Book>()
                    for (l in snapshot.children) {
                        val key = l.key
                        Log.d("title", key!!)
                        val z = l.getValue(Book::class.java)
                        if(z!!.bookClass == currentPos){
                            x.add(z)
                            Log.d("x", x.toString())

                            holder.recycler.adapter = StoreBookAdapter(c!!, x)
        //                    Log.d("items", ada.itemCount.toString() )
                            val layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
                            layoutManager.initialPrefetchItemCount = book.size
                            holder.recycler.layoutManager = layoutManager

                        }

                    }
                    //x.clear()

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


//            holder.recycler.adapter = StoreBookAdapter(c!!, book)
//            Log.d("items", ada.itemCount.toString() )
//            val layoutManager = LinearLayoutManager(c, LinearLayoutManager.HORIZONTAL, false)
//            layoutManager.initialPrefetchItemCount = book.size
//            holder.recycler.layoutManager = layoutManager
            //recycler.setRecycledViewPool(viewPool)



        holder.itemView.setOnClickListener {

        }

    }

    override fun getItemCount(): Int {
        return  y.size
    }


}