package com.example.mylib.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.LessonAdapter
import com.example.mylib.adapters.LessonSectionAdapter
import com.example.mylib.adapters.SearchBookAdapter
import com.example.mylib.databinding.FragmentDashboardBinding
import com.example.mylib.model.Book
import com.example.mylib.model.Lesson
import com.example.mylib.model.Topic
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val bookRecycler = root.findViewById<RecyclerView>(R.id.bookRecycler)
        val lessonRecycler = root.findViewById<RecyclerView>(R.id.lessonRecycler)
        val x = ArrayList<Book>()
        val allBooks = ArrayList<Book>()
        val search = root.findViewById<SearchView>(R.id.searchView)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val bClass = arrayOf("Primary 1", "Primary 2", "Primary 3", "Primary 4", "Primary 5", "Primary 6", "Primary 7",)
                for (c in bClass){
                FirebaseDatabase.getInstance().getReference("/book").child(c)
                    .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        x.clear()
                        allBooks.clear()
                        for (i in snapshot.children){
                            val z = i.getValue(Book::class.java)

                            allBooks.add(z!!)
                            if (z.bookTitle!!.contains(query!!, true) || z.bookDescription!!.contains(query, true)){
                                x.add(z)
                            }
                        }

                        bookRecycler.adapter = SearchBookAdapter(requireContext(), x)
                        bookRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })
                }

                val tArray = arrayOf("Term 1", "Term 2", "Term 3",)
                val topicList = ArrayList<Topic>()
                if (allBooks.isNotEmpty()) {
                    for (b in allBooks) {
                        for (t in tArray) {
                            FirebaseDatabase.getInstance().getReference("/topic").child(b.bookTitle!!).child(t)
                                .addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        x.clear()
                                        for (k in snapshot.children) {
                                            val key = k.key
                                            Log.d("key", k.toString())
                                            val z = k.getValue(Topic::class.java)
                                            Log.d("z", z.toString())
                                            topicList.add(z!!)
                                            if(z.topicTitle == query || z.topicTitle!!.contains(query!!, true) || z.topicTheme!!.contains(query, true)){
                                                for (r in allBooks){
                                                    if (r.bookTitle == z.bookTitle){
                                                        FirebaseDatabase.getInstance().getReference("/book").child(z.bookClass!!)
                                                            .addValueEventListener(object : ValueEventListener{
                                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                                    for (i in snapshot.children){
                                                                        val q = i.getValue(Book::class.java)
                                                                        x.add(q!!)
                                                                    }

                                                                    bookRecycler.adapter = SearchBookAdapter(requireContext(), x)
                                                                    bookRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                                                                }

                                                                override fun onCancelled(error: DatabaseError) {

                                                                }

                                                            })
                                                    }
                                                }
                                            }

                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {

                                    }

                                })
                        }
                    }
                }

                val tLessons = ArrayList<String>()
                val less = ArrayList<String>()
                if (topicList.isNotEmpty()){
                    for (topic in topicList){
                        FirebaseDatabase.getInstance().getReference("/lesson").child(topic.bookTitle!!).child(topic.termTitle!!).
                        child(topic.topicTitle!!)
                            .addValueEventListener(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    val z = snapshot.value as HashMap<String, ArrayList<Lesson>>

                                    val k = z.keys
                                    k.forEach {

                                        Log.d("keys", it)

                                        if (it == query || it.contains(query!!, true)){
                                            less.add(it)
                                            if (less.isNotEmpty()){
                                                    Log.d("snapshot", snapshot.toString())
                                                    val bAdapter = LessonAdapter(requireContext(), less, topic.bookTitle!!, topic.topicTitle!!, topic.termTitle!!, topic.bookClass!!)
                                                    lessonRecycler.adapter = bAdapter
                                                    lessonRecycler.layoutManager = LinearLayoutManager(requireContext())


                                            }
                                            for (y in allBooks){
                                                if(y.bookTitle!! == topic.bookTitle){
                                                    FirebaseDatabase.getInstance().getReference("/book").child(y.bookClass!!).child(y.bookTitle!!)
                                                        .addValueEventListener(object : ValueEventListener{
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                for (i in snapshot.children){
                                                                    val q = i.getValue(Book::class.java)
                                                                    x.add(q!!)
                                                                }

                                                                bookRecycler.adapter = SearchBookAdapter(requireContext(), x)
                                                                bookRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                                                            }

                                                            override fun onCancelled(error: DatabaseError) {

                                                            }

                                                        })
                                                }

                                            }
                                        }
                                        else{
                                            tLessons.add(it)

                                            if (tLessons.isNotEmpty()){

                                                val sec = ArrayList<String>()
                                            for (w in tLessons){
                                                FirebaseDatabase.getInstance().getReference("/lesson").child(topic.bookTitle!!).child(topic.termTitle!!).
                                                child(topic.topicTitle!!).child(w)
                                                    .addValueEventListener(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            for (d in snapshot.children){
                                                                val a = d.getValue(Lesson::class.java)
                                                                //t.add(z!!)
                                                                if (a!!.lessonContent!!.contains(query, true) || a.lessonTitle!!.contains(query, true)){
                                                                    sec.add(d.key!!)

                                                                    val bAdapter = LessonAdapter(requireContext(), less, topic.bookTitle!!, topic.topicTitle!!, topic.termTitle!!, topic.bookClass!!)
                                                                    lessonRecycler.adapter = bAdapter
                                                                    lessonRecycler.layoutManager = LinearLayoutManager(requireContext())

                                                                            FirebaseDatabase.getInstance().getReference("/book").child(topic.bookClass!!).child(topic.bookTitle!!)
                                                                                .addValueEventListener(object : ValueEventListener{
                                                                                    override fun onDataChange(snapshot: DataSnapshot) {
                                                                                        for (i in snapshot.children){
                                                                                            val q = i.getValue(Book::class.java)
                                                                                            x.add(q!!)
                                                                                        }

                                                                                        bookRecycler.adapter = SearchBookAdapter(requireContext(), x)
                                                                                        bookRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

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
                                              }

                                            }
                                    }
                                    }


                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
                    }
                }








                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {
              return false
            }

        })
        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}