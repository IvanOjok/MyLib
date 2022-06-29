package com.example.mylib.ui.library

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.BookTermAdapter
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Book
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.Term
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BookTermFragment : Fragment() {

    private val prefsManager = PrefsManager.INSTANCE
    var termsRecycler: RecyclerView ?= null

    var id:String?=null
    var title:String?=null
    var term:String?=null
    var price:String?=null
    var url :String?=null
    var titleTerm :String?=null
    var bookClass :String?=null
    var color:String = "faff0cac"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root =  inflater.inflate(R.layout.fragment_book_term, container, false)

        val bundle = arguments
        if (bundle != null){

            id =bundle.getString("id")
            title =bundle.getString("title")
            term = bundle.getString("term")
            price =bundle.getString("price")
            url = bundle.getString("url")
            titleTerm = bundle.getString("titleTerm")
            bookClass =bundle.getString("bookClass")
        }


        val bar = root.findViewById<MaterialToolbar>(R.id.bar)

        val terms = root.findViewById<TextView>(R.id.terms)
        terms.setOnClickListener {
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
        }


        val topics = root.findViewById<TextView>(R.id.topics)
        topics.setOnClickListener {

            val purchases = BookTopicFragment()
            val bundler = Bundle()
            bundler.putString("title", title)
            bundler.putString("term", term)
            bundler.putString("price", price)
            bundler.putString("url", url)
            bundler.putString("bClass", bookClass)
            bundler.putString("id", id)
            bundler.putString("titleTerm", titleTerm)
//            bundle.putString("color", y[position].bookColor)
//            bundle.putString("lessons", y[position].lessons)
//            bundle.putString("topics", y[position].topics)

            purchases.arguments = bundler

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()
        }



        val bookTitle = root.findViewById<TextView>(R.id.title)
        bookTitle.text = title

        val description = root.findViewById<TextView>(R.id.description)
        termsRecycler = root.findViewById<RecyclerView>(R.id.termsRecycler)

        FirebaseDatabase.getInstance().getReference("/book").child(bookClass!!).child(title!!).addValueEventListener(object :
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

        val detail = ArrayList<Term>()
        if (term == "Term 1, Term 2, Term 3"){
            getTerms(title!!)
        }
        else{

        FirebaseDatabase.getInstance().getReference("/terms").child(title!!).child(term!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val z = snapshot.getValue(Term::class.java)
                detail.add(z!!)

                val bAdapter = BookTermAdapter(requireContext(), detail, color)
                termsRecycler!!.visibility = View.VISIBLE
                termsRecycler!!.adapter = bAdapter
                termsRecycler!!.layoutManager = LinearLayoutManager(requireContext())

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error terms", error.toString())
            }

           })
        }


            return root
    }

    fun getTerms(titleText:String){
        val p = ArrayList<Term>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/terms").child(titleText).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                            Log.d("snap", "$snapshot")
                            for (k in snapshot.children) {
                                val key = k.key
                                Log.d("key", k.toString())
                                val z = k.getValue(Term::class.java)
                                Log.d("z", z.toString())
                                //if(titleText == key){
                                    p.add(z!!)
                               // }

                            }
                            val bAdapter = BookTermAdapter(requireContext(), p, color)
                            termsRecycler!!.visibility = View.VISIBLE
                            termsRecycler!!.adapter = bAdapter
                            termsRecycler!!.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                //LinearLayoutManager(requireContext())

                            // }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext(), "A 2nd $error occured", Toast.LENGTH_LONG).show()

                        }

                    })
    }


}