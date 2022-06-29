package com.example.mylib.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.*
import com.example.mylib.databinding.FragmentHomeBinding
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Book
import com.example.mylib.model.Lesson
import com.example.mylib.model.Library
import com.example.mylib.model.PrefsManager
import com.example.mylib.ui.SettingsActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        prefsManager.setContext(requireActivity().application)
        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name

        val settings = root.findViewById<ImageView>(R.id.settings)
        settings.setOnClickListener {
            startActivity(Intent(activity, SettingsActivity::class.java))
        }

        val textView2 = root.findViewById<TextView>(R.id.textView2)
        textView2.text = "You're welcome $profileName"


        val scroll = root.findViewById<ScrollView>(R.id.scroll)
        val newUser = root.findViewById<ConstraintLayout>(R.id.newUser)
        //existing users
        FirebaseDatabase.getInstance().getReference("/library").child(phone!!).child(profileName!!)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                       scroll.visibility = View.VISIBLE
                        newUser.visibility = View.INVISIBLE

        //continue reading
        val bookRecycler = root.findViewById<RecyclerView>(R.id.bookRecycler)
        FirebaseDatabase.getInstance().getReference("/progress").child(phone)
            .child(profileName).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val titles = ArrayList<String>()
                        val r = snapshot.value as HashMap<String, Book>

                        val k = r.keys
                        for (i in k) {
                            titles.add(i)
                            Log.d("keys", i)
                            Log.d("bookRec", r.get(i).toString())

                            FirebaseDatabase.getInstance().getReference("/progress").child(phone)
                                .child(profileName).child(i).addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val terms = ArrayList<String>()
                                        val term = snapshot.value as HashMap<String, Book>

                                        val kTerm = term.keys
                                        for (j in kTerm) {
                                            terms.add(i)
                                            Log.d("keys", i)
                                            Log.d("bookRec", r.get(i).toString())

                                            FirebaseDatabase.getInstance().getReference("/progress").child(phone)
                                                .child(profileName).child(i).child(j).addValueEventListener(object : ValueEventListener{
                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                        val topics = ArrayList<String>()
                                                        val topic =
                                                            snapshot.value as HashMap<String, Book>

                                                        val kTopic = topic.keys
                                                        for (l in kTopic) {
                                                            topics.add(l)
                                                            Log.d("keys", i)
                                                            Log.d("bookRec", r.get(i).toString())

                                                            FirebaseDatabase.getInstance().getReference("/progress").child(phone)
                                                                .child(profileName).child(i).child(j).child(l).addValueEventListener(
                                                                    object : ValueEventListener{
                                                                        override fun onDataChange(
                                                                            snapshot: DataSnapshot
                                                                        ) {
                                                                            val lessons = ArrayList<String>()
                                                                            val lesson =
                                                                                snapshot.value as HashMap<String, Book>

                                                                            val kLesson = lesson.keys
                                                                            for (m in kLesson) {
                                                                                val t = "$m in $l from $i"
                                                                                lessons.add(t)
                                                                                Log.d("keys", i)
                                                                                Log.d("bookRec",
                                                                                    r.get(i)
                                                                                        .toString()

                                                                                )
                                                                            }

                                                                            val layoutManager = LinearLayoutManager(
                                                                                requireContext(),
                                                                                LinearLayoutManager.HORIZONTAL,
                                                                                false
                                                                            )
                                                                            //layoutManager.initialPrefetchItemCount = book[position].bookDetail!!.size
                                                                            val adapter = HomeBookAdapter(requireContext(), lessons)
                                                                            //Log.d("book", "$x")
                                                                            bookRecycler.adapter = adapter
                                                                            bookRecycler.layoutManager = layoutManager

                                                                        }

                                                                        override fun onCancelled(
                                                                            error: DatabaseError
                                                                        ) {

                                                                        }

                                                                    }
                                                                )

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
                    else{
                        val noBook = root.findViewById<TextView>(R.id.noBook)
                        noBook.text = "No lessons completed"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })





        //mybooks
        val lessonRecycler = root.findViewById<RecyclerView>(R.id.lessonRecycler)

        FirebaseDatabase.getInstance().getReference("/library").child(phone).child(profileName)
            .addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        val t = ArrayList<String>()
                        val r = snapshot.value as HashMap<String, Book>

                        val k = r.keys
                        for (i in k) {
                            t.add(i)
                            Log.d("keys", i)
                            Log.d("bookRec", r.get(i).toString())

                            val x = ArrayList<Library>()
                            FirebaseDatabase.getInstance().getReference("/library").child(phone).child(profileName).child(i)
                                .addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for (lib in snapshot.children) {
                                            val key = lib.key
                                            Log.d("y", "$k")
                                            Log.d("key", "$key")
                                            val z = lib.getValue(Library::class.java)
                                            Log.d("z", "$z")
                                            Log.d("z", "${z!!.startDate}")
                                            Log.d("z", "${z.endDate}")
                                            x.add(z)

                                            val layoutManager = LinearLayoutManager(
                                              requireContext(),
                                                LinearLayoutManager.HORIZONTAL,
                                                false
                                            )
                                            //layoutManager.initialPrefetchItemCount = book[position].bookDetail!!.size
                                            val adapter = LibraryBookAdapter(requireContext(), x)
                                            Log.d("book", "$x")
                                            lessonRecycler.adapter = adapter
                                            lessonRecycler.layoutManager = layoutManager

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                })

                        }

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "A 2nd $error occured", Toast.LENGTH_LONG)
                        .show()

                }

            })


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

                                                var ref = FirebaseDatabase.getInstance().getReference("/readingList").child(phone).child(profileName).child(it)
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
                                        readingList.adapter = bAdapter
                                        readingList.layoutManager = LinearLayoutManager(
                                            requireContext(),
                                            LinearLayoutManager.HORIZONTAL,
                                            false
                                        )
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            }
                        )

        }

                    ///new user on app
        else{

                        scroll.visibility = View.INVISIBLE
                        newUser.visibility = View.VISIBLE


                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        val c = activity as HomeActivity
        c.supportFragmentManager.beginTransaction().replace(R.id.frag_container, this).addToBackStack(null).commit()
        super.onAttach(context)
    }
}