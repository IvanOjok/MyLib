package com.example.mylib.ui.library

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.LessonAdapter
import com.example.mylib.adapters.LessonSectionAdapter
import com.example.mylib.adapters.ReadingListAdapter
import com.example.mylib.model.Book
import com.example.mylib.model.Lesson
import com.example.mylib.model.PrefsManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ReadingListFragment : Fragment() {

    private val prefsManager = PrefsManager.INSTANCE

    var bookTitle:String ?= null
    var topicTitle:String ?= null
    var termTitle:String ?= null
    var bookClass:String ?= null
    var lessonId:String ?= null
    lateinit var bAdapter:ReadingListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_reading_list, container, false)

        val bundle = arguments
        if (bundle != null) {
            bookTitle = bundle.getString("bookTitle")
            topicTitle = bundle.getString("topicTitle")
            termTitle = bundle.getString("termTitle")
            bookClass = bundle.getString("bookClass")
            lessonId = bundle.getString("lessonId")

            Log.d("bundle", bookTitle!!)
        }

        prefsManager.setContext(requireActivity().application)
        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name
        val newList = root.findViewById<Button>(R.id.newList)

        val readingList = root.findViewById<RecyclerView>(R.id.readingList)
        val lesson = ArrayList<Lesson>()
        var s = "0"
        FirebaseDatabase.getInstance().getReference("/readingList").child(phone!!).child(profileName!!).addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val t = ArrayList<String>()
                        val z = snapshot.value as HashMap<String, ArrayList<Lesson>>

                        val k = z.keys
                        k.forEach {

                            Log.d("keys", it)
                            if (it == "none"){
                                s = "0"
                            }
                            else{
                                t.add(it)

                                val ref = FirebaseDatabase.getInstance().getReference("/readingList").child(phone).child(profileName).child(it)
                                    ref.addValueEventListener(
                                    object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            s = "${snapshot.childrenCount}"
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

                                        override fun onCancelled(error: DatabaseError) {

                                        }

                                    })
                            }

                        }

                        //val l = z.values.size.toString()

                        Log.d("snapshot", snapshot.toString())
                        if(bookTitle != null && topicTitle != null && termTitle != null && bookClass != null && lessonId != null){
                            bAdapter = ReadingListAdapter(requireContext(), t, s, phone, profileName, bookTitle!!, topicTitle!!, termTitle!!, bookClass!!, lessonId!!)
                        }
                        else{
                            bAdapter = ReadingListAdapter(requireContext(), t, s,)
                        }
                        readingList.adapter = bAdapter
                        readingList.layoutManager = LinearLayoutManager(requireContext())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            }
        )









            newList.setOnClickListener {

                if(bookTitle != null && topicTitle != null && termTitle != null && bookClass != null && lessonId != null){
                val eT = EditText(requireContext())
                AlertDialog.Builder(requireContext()).setTitle("Enter List name").setView(eT).
                setPositiveButton("Save", object :DialogInterface.OnClickListener {
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        val text = eT.text.toString()
                        if (text.isNotEmpty()) {

                            val lessArray = ArrayList<Lesson>()
                            FirebaseDatabase.getInstance().getReference("/lesson")
                                .child(bookTitle!!).child(termTitle!!).child(topicTitle!!)
                                .child(lessonId!!)     //.orderByChild("bookClass").equalTo(bClass)
                                .addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(
                                        snapshot: DataSnapshot
                                    ) {
                                        for (ttLesson in snapshot.children) {
                                            val q = ttLesson.getValue(Lesson::class.java)
                                            lessArray.add(q!!)

                                        }
                                        FirebaseDatabase.getInstance().getReference("/readingList")
                                            .child(phone)
                                            .child(profileName).child(text).child(lessonId!!)
                                            .setValue(lessArray)

                                    }

                                    override fun onCancelled(
                                        error: DatabaseError
                                    ) {

                                    }

                                })
                        }

                        else{
                            Toast.makeText(requireContext(), "Give list name", Toast.LENGTH_SHORT).show()
                        }

                    }

                }).setOnCancelListener { p0 -> p0!!.dismiss() }.show()
                }
                else{
                    val eT = EditText(requireContext())
                    AlertDialog.Builder(requireContext()).setTitle("Enter List name").setView(eT).
                    setPositiveButton("Save", object :DialogInterface.OnClickListener{
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val t = eT.text.toString()
                            if (t.isNotEmpty()){
                                FirebaseDatabase.getInstance().getReference("/readingList").child(phone)
                                    .child(profileName).child(t).child("none").setValue(Lesson("none", "none", "none", "none", "none", "none", "none", "none", "none"))
                            }

                        }

                    }).setOnCancelListener { p0 -> p0!!.dismiss() }.show()


                }

            }



        val cancel = root.findViewById<ImageView>(R.id.cancel)
        cancel.setOnClickListener {
            requireFragmentManager().popBackStackImmediate()
        }


        return root
    }

}