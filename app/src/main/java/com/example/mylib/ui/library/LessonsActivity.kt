package com.example.mylib.ui.library

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.LessonSectionAdapter
import com.example.mylib.model.Book
import com.example.mylib.model.Lesson
import com.example.mylib.ui.home.HomeFragment
import com.example.mylib.ui.store.StoreFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LessonsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)

        val bTitle = intent.getStringExtra("bookTitle")
        val bTopic = intent.getStringExtra("topicTitle")
        val bTerm = intent.getStringExtra("termTitle")
        val bClass = intent.getStringExtra("bookClass")
        val lesson = intent.getStringExtra("lessonId")


        val bookTitle = findViewById<TextView>(R.id.title)
        bookTitle.text = lesson

        var color:String = "faff0cac"
        val bar = findViewById<MaterialToolbar>(R.id.bar)

        FirebaseDatabase.getInstance().getReference("/book").child(bClass!!).child(bTitle!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val z = snapshot.getValue(Book::class.java)
                color = z!!.bookColor!!
                bar.setBackgroundColor(Color.parseColor(color))

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })


        val lessonRecycler = findViewById<RecyclerView>(R.id.lessonRecycler)

        val t = ArrayList<Lesson>()
        FirebaseDatabase.getInstance().getReference("/lesson").child(bTitle).child(bTerm!!).child(bTopic!!).child(lesson!!)     //.orderByChild("bookClass").equalTo(bClass)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (k in snapshot.children){
                        val z = k.getValue(Lesson::class.java)
                        t.add(z!!)

                        val bAdapter = LessonSectionAdapter(this@LessonsActivity, t)
                        lessonRecycler.adapter = bAdapter
                        lessonRecycler.layoutManager = LinearLayoutManager(this@LessonsActivity)

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Error", "$error")
                }
            })

        val quiz = findViewById<Button>(R.id.quiz)
        quiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("bookTitle", bTitle)
            intent.putExtra("topicTitle", bTopic)
            intent.putExtra("termTitle", bTerm)
            intent.putExtra("bookClass", bClass)
            intent.putExtra("lessonId", lesson)

            startActivity(intent)
        }


        ///add reading List
        val addToReadingList = findViewById<ImageView>(R.id.addToReadingList)
        addToReadingList.setOnClickListener {
            //val x = con as HomeActivity
            quiz.visibility = View.INVISIBLE
            val readList = ReadingListFragment()
            val bundle = Bundle()
            bundle.putString("bookTitle", bTitle)
            bundle.putString("topicTitle", bTopic)
            bundle.putString("termTitle", bTerm)
            bundle.putString("bookClass", bClass)
            bundle.putString("lessonId", lesson)

            readList.arguments = bundle
            this.supportFragmentManager.beginTransaction().replace(R.id.lessons_container, readList).addToBackStack(null).commit()

        }



        val navigation = findViewById<BottomNavigationView>(R.id.nav_library_view)

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
              //   R.id.navigation_back -> setFragment(HomeFragment())
                R.id.navigation_library -> setFragment(NotificationsFragment())
                R.id.navigation_lessons -> {
                    finish()
                    startActivity(intent)
                }
               // R.id.navigation_next -> setFragment(StoreFragment())
            }
            true
        }

    }


    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.lessons_container, fragment)
                .commit()
        }

    }
}