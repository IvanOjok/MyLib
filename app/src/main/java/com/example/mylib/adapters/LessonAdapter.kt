package com.example.mylib.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.ui.library.LessonsActivity

class LessonAdapter(): RecyclerView.Adapter<LessonAdapter.LessonViewHolder>(){
    var c: Context?= null
    var lesson = ArrayList<String>()
    var bTitle:String ?= null
    var bTopic:String ?= null
    var bTerm:String ?= null
    var bClass:String ?= null

    constructor(r: Context, lesson: ArrayList<String>,  title:String, topic:String, term:String, bClass:String) : this() {
        this.c = r
        this.lesson = lesson
        this.bTitle = title
        this.bTopic = topic
        this.bTerm = term
        this.bClass = bClass
    }


    class LessonViewHolder( itemView: View) : RecyclerView.ViewHolder(itemView) {

        //fun onBind(bookClass:String, name:String){

        val name = itemView.findViewById<TextView>(R.id.text)
        //n.text = name

        //val recycler = itemView.findViewById<RecyclerView>(R.id.books)

        // }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return LessonViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {


        holder.name.text = lesson[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(c, LessonsActivity::class.java)
            intent.putExtra("bookTitle", bTitle)
            intent.putExtra("topicTitle", bTopic)
            intent.putExtra("termTitle", bTerm)
            intent.putExtra("bookClass", bClass)
            intent.putExtra("lessonId", lesson[position])

             c!!.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {

        return  lesson.size
    }

}