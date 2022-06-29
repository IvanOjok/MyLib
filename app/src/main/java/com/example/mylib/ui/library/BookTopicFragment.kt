package com.example.mylib.ui.library

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.BookTopicAdapter
import com.example.mylib.model.Book
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.Topic
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BookTopicFragment : Fragment() {
    private val prefsManager = PrefsManager.INSTANCE

    var title:String?=null
    var term:String?=null
    var price:String?=null
    var url :String?=null
    var bClass :String?=null
    var color:String = "faff0cac"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_book_topic, container, false)

        val bundle = arguments
        if (bundle != null){
            title =bundle.getString("title")
            term = bundle.getString("term")
            price =bundle.getString("price")
            url = bundle.getString("url")
            bClass = bundle.getString("bClass")
        }

        val terms = root.findViewById<TextView>(R.id.terms)
        terms.setOnClickListener {

            val purchases = BookTermFragment()
            val bundler = Bundle()
            //bundler.putString("id", id)
            bundler.putString("title", title)
            bundler.putString("term", term)
            bundler.putString("price", price)
            bundler.putString("url", url)
            //bundler.putString("titleTerm", titleTerm)
            bundler.putString("bookClass", bClass)

            purchases.arguments = bundler

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()

        }


        val topics = root.findViewById<TextView>(R.id.topics)
        topics.setOnClickListener {
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
        }



        val topicRecycler = root.findViewById<RecyclerView>(R.id.topicRecycler)


        val bookTitle = root.findViewById<TextView>(R.id.title)
        bookTitle.text = title

        val description = root.findViewById<TextView>(R.id.description)
        val bar = root.findViewById<MaterialToolbar>(R.id.bar)

        FirebaseDatabase.getInstance().getReference("/book").child(bClass!!).child(title!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val z = snapshot.getValue(Book::class.java)
                val des = z!!.bookDescription
                description.text = des

                color = z.bookColor!!
                bar.setBackgroundColor(Color.parseColor(color))

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
        //07

        val t = ArrayList<Topic>()
        FirebaseDatabase.getInstance().getReference("/topic").child(title!!).child(term!!).addValueEventListener(object :
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
                if(t.isNotEmpty()){
                    val bAdapter = BookTopicAdapter(requireContext(), t)
                    topicRecycler!!.visibility = View.VISIBLE
                    topicRecycler.adapter = bAdapter
                    topicRecycler.layoutManager =
                        LinearLayoutManager(requireContext())

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return root
    }


}