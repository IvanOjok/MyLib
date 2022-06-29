package com.example.mylib.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R

class AdminLessons : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_lessons)

        val bTitle = intent.getStringExtra("bookTitle")
        val bTopic = intent.getStringExtra("topicTitle")
        val bTerm = intent.getStringExtra("termTitle")
        val bClass = intent.getStringExtra("bookClass")


        val lessonRecycler = findViewById<RecyclerView>(R.id.lessonRecycler)



        val addLesson = findViewById<ImageView>(R.id.addLesson)
        addLesson.setOnClickListener {
            val intent = Intent(this, AdminAddLessons::class.java)
            intent.putExtra("bTitle", bTitle)
            intent.putExtra("bTopic", bTopic)
            intent.putExtra("bTerm", bTerm)
            intent.putExtra("bClass", bClass)
            startActivity(intent)
        }
    }
}