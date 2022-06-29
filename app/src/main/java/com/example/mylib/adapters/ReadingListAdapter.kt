package com.example.mylib.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Lesson
import com.example.mylib.ui.library.LessonsActivity
import com.example.mylib.ui.library.ReadingListLessonsFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ReadingListAdapter(): RecyclerView.Adapter<ReadingListAdapter.ReadingListVH>(){
    var c: Context?= null
    var book = ArrayList<String>()
    var lessons:String ?= null
    var bookTitle:String ?= null
    var topicTitle:String ?= null
    var termTitle:String ?= null
    var bookClass:String ?= null
    var lessonId:String ?= null

    var phone:String ?= null
    var profileName:String ?= null


    constructor(r: Context, details: ArrayList<String>, lessons:String) : this() {
        this.c = r
        this.book = details
        this.lessons = lessons
    }

    constructor(r: Context, details: ArrayList<String>, lessons:String, phone:String, profileName:String, bookTitle:String, topicTitle:String, termTitle:String,
                bookClass: String, lessonId:String) : this() {
        this.c = r
        this.book = details
        this.lessons = lessons
        this.bookTitle = bookTitle
        this.topicTitle = topicTitle
        this.termTitle = termTitle
        this.bookClass = bookClass
        this.lessonId = lessonId
        this.phone = phone
        this.profileName = profileName
    }

    class ReadingListVH( itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val term = itemView.findViewById<TextView>(R.id.term)
        val image = itemView.findViewById<ImageView>(R.id.image)
        val remove = itemView.findViewById<ImageView>(R.id.remove)
        val number = itemView.findViewById<TextView>(R.id.price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadingListVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.cart_list, parent, false)
        return ReadingListVH(inflate)
    }

    override fun onBindViewHolder(holder: ReadingListVH, position: Int) {
        holder.title.text = book[position]
        holder.title.setTextColor(c!!.resources.getColor(R.color.black))
        holder.term.visibility = View.INVISIBLE
        holder.remove.visibility = View.INVISIBLE
        holder.number.text = lessons
        holder.number.setTextColor(c!!.resources.getColor(R.color.black))

        holder.itemView.setOnClickListener {
            if(bookTitle!= null && topicTitle != null && termTitle != null && bookClass != null && lessonId != null){

               val ref =  FirebaseDatabase.getInstance().getReference("/readingList").child(phone!!)
                    .child(profileName!!).child(book[holder.adapterPosition])

                ref.addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            for (l in snapshot.children){
                                if (l.key == "none"){
                                    ref.child("none").removeValue()
                                }
                                else{
                                    val lessArray = ArrayList<Lesson>()
                                    FirebaseDatabase.getInstance().getReference("/lesson").child(bookTitle!!).child(termTitle!!).child(topicTitle!!).child(lessonId!!)     //.orderByChild("bookClass").equalTo(bClass)
                                        .addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(
                                                snapshot: DataSnapshot
                                            ) {
                                                for(ttLesson in snapshot.children){
                                                    val q = ttLesson.getValue(Lesson::class.java)
                                                    lessArray.add(q!!)

                                                }
                                                FirebaseDatabase.getInstance().getReference("/readingList").child(phone!!)
                                                    .child(profileName!!).child(book[holder.adapterPosition]).child(lessonId!!).setValue(lessArray)


                                                Toast.makeText(c, "Lesson Added", Toast.LENGTH_SHORT).show()

                                            }

                                            override fun onCancelled(
                                                error: DatabaseError
                                            ) {

                                            }

                                        })

                                }
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })


            }
//            else {
                val x = c as LessonsActivity
                val cart = ReadingListLessonsFragment()
                val bundle = Bundle()
                bundle.putString("title", book[position])


            cart.arguments = bundle
            x.supportFragmentManager.beginTransaction().replace(R.id.lessons_container, cart).addToBackStack(null).commit()
          //  }
        }
    }

    override fun getItemCount(): Int {
        return  book.size
    }

}