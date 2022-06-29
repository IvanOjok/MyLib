package com.example.mylib.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Library
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LibraryClassAdapter(): RecyclerView.Adapter<LibraryClassAdapter.LibraryClassViewHolder>(){
    var y = ArrayList<String>()
    var c: Context?= null
    var pho:String ?= null
    var pName:String ?= null

    constructor(r: Context, options: ArrayList<String>, phone:String, profileName:String) : this() {
        this.c = r
        this.y = options
        this.pho = phone
        this.pName = profileName
    }

    var viewPool = RecyclerView.RecycledViewPool()

    class LibraryClassViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        //fun onBind(bookClass:String, name:String){

        val name = itemView.findViewById<TextView>(R.id.className)
        //n.text = name

        val recycler = itemView.findViewById<RecyclerView>(R.id.books)

        // }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryClassViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.book_layout_list, parent, false)
        return LibraryClassViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: LibraryClassViewHolder, position: Int) {
        // holder.onBind(y[position].gender, y[position].name)

        val cur = y[position]
        holder.name.text =cur

        val x = ArrayList<Library>()
        FirebaseDatabase.getInstance().getReference("/library").child(pho!!).child(pName!!).child(cur)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (k in snapshot.children) {
                        val key = k.key
                        Log.d("y", "$k")
                        Log.d("key", "$key")
                        val z = k.getValue(Library::class.java)
                        Log.d("z", "$z")
                        Log.d("z", "${z!!.startDate}")
                        Log.d("z", "${z.endDate}")
                        x.add(z)

                        val layoutManager = LinearLayoutManager(
                            holder.recycler.context,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        //layoutManager.initialPrefetchItemCount = book[position].bookDetail!!.size
                        val adapter = LibraryBookAdapter(c!!, x)
                        Log.d("book", "$x")
                        holder.recycler.adapter = adapter
                        holder.recycler.layoutManager = layoutManager
                        holder.recycler.setRecycledViewPool(viewPool)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

    override fun getItemCount(): Int {
        return  y.size
    }

}