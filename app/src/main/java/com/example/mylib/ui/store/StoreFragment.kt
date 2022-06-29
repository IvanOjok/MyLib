package com.example.mylib.ui.store

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.StoreBookAdapter
import com.example.mylib.adapters.StoreClassAdapter
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.Book
import com.example.mylib.model.BookList
import com.example.mylib.model.Lesson
import com.google.firebase.database.*
import dmax.dialog.SpotsDialog


class StoreFragment : Fragment() {

    var dialog: AlertDialog? = null
    var key:String ?= null

    var bookTitle:String ?= null
    var bookTerm:String ?= null
    var bookPrice:String ?= null
    var bookUrl:String ?= null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_store, container, false)

        val books = root.findViewById<TextView>(R.id.books)
        books.setOnClickListener {
            Log.d("yeah", "works like magic")
            requireFragmentManager().beginTransaction().detach(this).attach(this).commit()
        }

        val cart = root.findViewById<TextView>(R.id.cart)
        cart.setOnClickListener {
            val purchases = CartFragment()
           requireActivity().supportFragmentManager.beginTransaction().replace(R.id.frag_container, purchases).addToBackStack(null).commit()
        }

        val storeRecycler = root.findViewById<RecyclerView>(R.id.storeRecycler)
        storeRecycler.layoutManager = LinearLayoutManager(requireContext())

        dialog = SpotsDialog.Builder().setContext(requireContext()).build()

        FirebaseDatabase.getInstance().getReference("/book").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("snap", "$snapshot")

                if (snapshot.exists()) {
                    val t = ArrayList<String>()
                    val c = HashMap<String, Book>()
                    val r = snapshot.value as HashMap<String, BookList>

                    val k = r.keys
                    val x = ArrayList<Book>()
                    for(i in k){
                        t.add(i)
                        Log.d("keys", i)
                        Log.d("bookRec", r.get(i).toString())

                        val bAdapter = StoreClassAdapter(
                            requireContext(),
                            t,
                        )
                        Log.d("books", bAdapter.book.toString())
                        storeRecycler.adapter = bAdapter
                        storeRecycler.layoutManager =
                            LinearLayoutManager(requireContext())

//                        FirebaseDatabase.getInstance().getReference("/book").child(i)
//                            .addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot) {
//                                    Log.d("data", snapshot.toString())
//
//                                    for (l in snapshot.children) {
//                                        key = l.key
//                                        Log.d("title", key!!)
//                                        val z = l.getValue(Book::class.java)
//                                        if(z!!.bookClass == i){
//                                            x.add(z)
//                                            Log.d("x", x.toString())
//
//                                            val bAdapter = StoreClassAdapter(
//                                                requireContext(),
//                                                t,
//                                                x
//                                            )
//                                            Log.d("books", bAdapter.book.toString())
//                                            storeRecycler.adapter = bAdapter
//                                            storeRecycler.layoutManager =
//                                                LinearLayoutManager(requireContext())
//
//                                        }
//
//                                    }
//                                    x.clear()
//
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                }
//
//                            })


                    }
//                    k.forEach {
//                        t.add(it)
//                        Log.d("keys", it)
//
//                        Log.d("bookRec", r.get(it).toString())
//
//
//                        FirebaseDatabase.getInstance().getReference("/book").child(it)
//                        .addValueEventListener(object : ValueEventListener {
//                            override fun onDataChange(snapshot: DataSnapshot) {
//                                Log.d("data", snapshot.toString())
//
//                                    for (l in snapshot.children) {
//                                        key = l.key
//                                        Log.d("title", key!!)
//                                        val z = l.getValue(Book::class.java)
//                                        if(z!!.bookClass == it){
//                                            x.add(z)
//                                             Log.d("x", x.toString())
//
//                                            val bAdapter = StoreClassAdapter(
//                                                requireContext(),
//                                                t,
//                                                x
//                                            )
//                                            Log.d("books", bAdapter.book.toString())
//                                            storeRecycler.adapter = bAdapter
//                                            storeRecycler.layoutManager =
//                                                LinearLayoutManager(requireContext())
//
//                                        }
//
//                                }
//
//                                //x.clear()
//
//                            }
//
//                            override fun onCancelled(error: DatabaseError) {
//                            }
//
//                        })
//
//                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "A 2nd $error occured", Toast.LENGTH_LONG).show()

            }

        })

        return root
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        var c:Context ?= null
//        if (c == null){
//            c = context as HomeActivity
//            c.setFragment(this)
//        }
//    }

}