package com.example.mylib.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.admin.AdminTopicAdapter
import com.example.mylib.model.Topic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminBookTopics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_book_topics)

        val term = intent.getStringExtra("term")
        val title = intent.getStringExtra("title")
        val bClass = intent.getStringExtra("class")

        val topicRecycler = findViewById<RecyclerView>(R.id.topicRecycler)


        val t = ArrayList<Topic>()
        FirebaseDatabase.getInstance().getReference("/topics").child(term!!).child(title!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (k in snapshot.children) {
                    val key = k.key
                    Log.d("key", k.toString())
                    val z = k.getValue(Topic::class.java)
                    Log.d("z", z.toString())
                    if(z!!.topicTitle == key){
                        t.add(z)
                    }

                }
                val bAdapter = AdminTopicAdapter(this@AdminBookTopics, t)
                topicRecycler!!.visibility = View.VISIBLE
                topicRecycler.adapter = bAdapter
                topicRecycler.layoutManager =
                    LinearLayoutManager(this@AdminBookTopics)

            }

             override fun onCancelled(error: DatabaseError) {

            }

        })


        val addTopic = findViewById<ImageView>(R.id.addTopic)
        addTopic.setOnClickListener {
            val intent = Intent(this, AdminAddTopics::class.java)
            intent.putExtra("bTitle", title)
            intent.putExtra("bTerm", term)
            intent.putExtra("bClass", bClass)
            startActivity(intent)
        }
    }
}