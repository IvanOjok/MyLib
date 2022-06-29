package com.example.mylib.ui.library

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.HomeReadListAdapter
import com.example.mylib.adapters.LibraryClassAdapter
import com.example.mylib.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NotificationsFragment : Fragment() {
    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var context: Context? = null

        val root: View = inflater.inflate(R.layout.fragment_notifications, container, false)

        prefsManager.setContext(requireActivity().application)
        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name

        val books = root.findViewById<TextView>(R.id.books)
        books.setOnClickListener {
            Log.d("yeah", "works like magic")
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
        }

        val libraryRecycler = root.findViewById<RecyclerView>(R.id.libraryRecycler)

        FirebaseDatabase.getInstance().getReference("/library").child(phone!!).child(profileName!!)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val t = ArrayList<String>()
                        val c = HashMap<String, Book>()
                        val r = snapshot.value as HashMap<String, Book>

                        val k = r.keys
                        val l = ArrayList<Book>()
                        for (i in k) {
                            t.add(i)
                            Log.d("keys", i)
                            Log.d("bookRec", r.get(i).toString())

                            val bAdapter = LibraryClassAdapter(requireContext(), t, phone, profileName)
                            libraryRecycler.adapter = bAdapter
                            Log.d("bAdapter", "${bAdapter.y}")
                            libraryRecycler.layoutManager = LinearLayoutManager(requireContext())

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "A 2nd $error occured", Toast.LENGTH_LONG)
                        .show()

                }

            })

        val readList = root.findViewById<TextView>(R.id.readList)
        readList.setOnClickListener {
            //reading lists

            val readingList = root.findViewById<RecyclerView>(R.id.listRecycler)
            val lesson = ArrayList<Lesson>()
            var s = "0"
            FirebaseDatabase.getInstance().getReference("/readingList").child(phone).child(profileName).addValueEventListener(
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
                                    s = t.size.toString()

                                    val ref = FirebaseDatabase.getInstance().getReference("/readingList").child(phone).child(profileName).child(it)
                                    ref.addValueEventListener(
                                        object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                s = "${snapshot.childrenCount}"
                                                val next =
                                                    snapshot.value as HashMap<String, ArrayList<Lesson>>

                                                val keyd = next.keys
                                                keyd.forEach {
                                                    ref.child("$it")
                                                        .addValueEventListener(object :
                                                            ValueEventListener {
                                                            override fun onDataChange(
                                                                snapshot: DataSnapshot
                                                            ) {
                                                                for (l in snapshot.children) {
                                                                    val f = l.getValue(
                                                                        Lesson::class.java
                                                                    )
                                                                    lesson.add(f!!)
                                                                }
                                                            }

                                                            override fun onCancelled(
                                                                error: DatabaseError
                                                            ) {

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
                            val bAdapter = HomeReadListAdapter(requireContext(), t, s)
                            libraryRecycler.adapter = bAdapter
                            libraryRecycler.layoutManager = LinearLayoutManager(requireContext())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                }
            )

//            val purchases = ReadingListFragment()
//            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()
        }


        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        var c = getContext()
        if (c == null){
            c = context.applicationContext
        }
    }



}