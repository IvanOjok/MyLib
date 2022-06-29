package com.example.mylib.ui.library

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.LessonAdapter
import com.example.mylib.adapters.ReadListLessonAdapter
import com.example.mylib.model.Lesson
import com.example.mylib.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReadingListLessonsFragment : Fragment() {

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_reading_list_lessons, container, false)

        prefsManager.setContext(requireActivity().application)
        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name

        val title = requireArguments().getString("title")

        val readingListLessons = root.findViewById<RecyclerView>(R.id.readingListLessons)
        val lesson = ArrayList<Lesson>()
        FirebaseDatabase.getInstance().getReference("/readingList").child(phone!!).child(profileName!!).child(title!!).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val t = ArrayList<String>()
                    val z = snapshot.value as HashMap<String, ArrayList<Lesson>>

                    val k = z.keys
                    k.forEach {
                        if (it == "none") {
                            Toast.makeText(requireContext(), "no lessons found", Toast.LENGTH_SHORT).show()
                        } else {
                        t.add(it)
                        Log.d("keys", it)

                       val ref = FirebaseDatabase.getInstance().getReference("/readingList").child(phone)
                            .child(profileName).child(title)
                            ref.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        val next = snapshot.value as HashMap<String, ArrayList<Lesson>>

                                        val keyd = next.keys
                                        keyd.forEach {
                                            ref.child("$it").addValueEventListener(object : ValueEventListener{
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    for (l in snapshot.children){
                                                        val f = l.getValue(Lesson::class.java)
                                                        lesson.add(f!!)
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {

                                                }

                                            })
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })

                        //lesson = z.get(it)!!
                    }
                }

                    Log.d("snapshot", snapshot.toString())
                    val bAdapter = ReadListLessonAdapter(requireContext(), t, lesson)
                    readingListLessons.adapter = bAdapter
                    readingListLessons.layoutManager = LinearLayoutManager(requireContext())
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        return root
    }

}