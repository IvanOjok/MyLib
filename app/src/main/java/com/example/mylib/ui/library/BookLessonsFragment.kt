package com.example.mylib.ui.library

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.LessonAdapter
import com.example.mylib.model.Book
import com.example.mylib.model.Lesson
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BookLessonsFragment : Fragment() {
    var color:String = "faff0cac"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_book_lessons, container, false)

        val bTitle = requireArguments().getString("bookTitle")
        val bTopic = requireArguments().getString("topicTitle")
        val bTerm = requireArguments().getString("termTitle")
        val bClass = requireArguments().getString("bookClass")


        val bookTitle = root.findViewById<TextView>(R.id.title)
        bookTitle.text = bTopic

        val bar = root.findViewById<MaterialToolbar>(R.id.bar)

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




        val lessonRecycler = root.findViewById<RecyclerView>(R.id.lessonRecycler)


        FirebaseDatabase.getInstance().getReference("/lesson").child(bTitle).child(bTerm!!).child(bTopic!!)     //.orderByChild("bookClass").equalTo(bClass)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //for (k in snapshot.children){

                    val t = ArrayList<String>()
                    val z = snapshot.value as HashMap<String, ArrayList<Lesson>>

                    val k = z.keys
                    k.forEach {
                        t.add(it)
                        Log.d("keys", it)
                    }

                    if (t.isNotEmpty()){
                        Log.d("snapshot", snapshot.toString())
                        val bAdapter = LessonAdapter(requireContext(), t, bTitle, bTopic, bTerm, bClass)
                        lessonRecycler.adapter = bAdapter
                        lessonRecycler.layoutManager = LinearLayoutManager(requireContext())

                    }

                   //}

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Error", "$error")
                }
            })
        return root
    }

}