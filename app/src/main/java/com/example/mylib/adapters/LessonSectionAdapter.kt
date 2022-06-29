package com.example.mylib.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mylib.R
import com.example.mylib.VideoView
import com.example.mylib.model.Lesson

class LessonSectionAdapter(): RecyclerView.Adapter<LessonSectionAdapter.LessonSectionVH>(){
    var c: Context?= null
    var book = ArrayList<Lesson>()

    constructor(r: Context, details: ArrayList<Lesson>) : this() {
        this.c = r
        this.book = details
    }

    class LessonSectionVH( itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sectiontitle = itemView.findViewById<TextView>(R.id.sectionTitle)
        val sectionContent = itemView.findViewById<TextView>(R.id.sectionContent)
        val sectionImage = itemView.findViewById<ImageView>(R.id.sectionImage)
        val sectionVideo = itemView.findViewById<VideoView>(R.id.sectionVideo)
        val mediaCaption = itemView.findViewById<TextView>(R.id.mediaCaption)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonSectionVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.lesson_section_list, parent, false)
        return LessonSectionVH(inflate)
    }

    override fun onBindViewHolder(holder: LessonSectionVH, position: Int) {
        holder.sectiontitle.text = book[position].lessonTitle
        holder.mediaCaption.text = book[position].mediaCaption
        holder.sectionContent.text = book[position].lessonContent

        if(book[position].mediaType == "video"){
            holder.sectionVideo.setVideoPath(book[position].lessonMedia!!)
            holder.sectionVideo.start()
        }
        else if (book[position].mediaType == "image"){
            Glide.with(c!!).load(book[position].lessonMedia).into(holder.sectionImage)
            holder.sectionVideo.visibility = View.INVISIBLE
        }
        else {
            holder.mediaCaption.visibility = View.INVISIBLE
            holder.sectionImage.visibility = View.INVISIBLE
            holder.sectionVideo.visibility = View.INVISIBLE
        } 

    }

    override fun getItemCount(): Int {
        return  book.size
    }

}