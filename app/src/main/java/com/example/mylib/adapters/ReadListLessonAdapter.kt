package com.example.mylib.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.model.Lesson
import com.example.mylib.ui.library.LessonsActivity

class ReadListLessonAdapter(): RecyclerView.Adapter<ReadListLessonAdapter.ReadListLessonVH>() {
    var c: Context? = null
    var lesson = ArrayList<String>()
    var sections= ArrayList<Lesson>()

    constructor(
        r: Context,
        lesson: ArrayList<String>,
        sections:ArrayList<Lesson>
    ) : this() {
        this.c = r
        this.lesson = lesson
       this.sections = sections
    }


    class ReadListLessonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById<TextView>(R.id.text)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReadListLessonVH {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.list_items, parent, false)
        return ReadListLessonVH(inflate)
    }

    override fun onBindViewHolder(holder: ReadListLessonVH, position: Int) {


        holder.name.text = lesson[position]

        holder.itemView.setOnClickListener {
            val intent = Intent(c, LessonsActivity::class.java)
            intent.putExtra("bookTitle", sections[position].bookTitle)
            intent.putExtra("topicTitle", sections[position].topicTitle)
            intent.putExtra("termTitle", sections[position].termTitle)
            intent.putExtra("bookClass", sections[position].bookClass)
            intent.putExtra("lessonId", lesson[position])

            c!!.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return lesson.size
    }
}