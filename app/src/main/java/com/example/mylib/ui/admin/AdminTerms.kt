package com.example.mylib.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.BookTermAdapter
import com.example.mylib.adapters.admin.AdminTermAdapter
import com.example.mylib.model.Term
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminTerms : AppCompatActivity() {
    lateinit var termRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_terms)

        val title = intent.getStringExtra("bTitle")
        val bClass = intent.getStringExtra("bookClass")
        val bUrl = intent.getStringExtra("bUrl")
        termRecycler = findViewById<RecyclerView>(R.id.termRecycler)
        getTerms(title!!)


        val addTerm = findViewById<ImageView>(R.id.addTerm)
        addTerm.setOnClickListener {
            val intent = Intent(this, AdminAddTerms::class.java)
            intent.putExtra("bTitle", title)
            intent.putExtra("bUrl", bUrl)
            intent.putExtra("bClass", bClass)
            startActivity(intent)
        }
    }

    fun getTerms(titleText:String){
        val p = ArrayList<String>()
        val dbRef = FirebaseDatabase.getInstance().getReference("/terms").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //dialog!!.dismiss()
                for (y in snapshot.children){
                    val key = y.key
                    Log.d("y", "$y")
                    val z = y.getValue(String()::class.java)
                    Log.d("z", "${z}")
                    p.add(z!!)
                    Log.d("p", "$p")
                }

                //val book = ArrayList<BookClass>()
                val detail = ArrayList<Term>()
                for (i in p){
                    FirebaseDatabase.getInstance().getReference("/termDetails").child(i).addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Log.d("snap", "$snapshot")
                            for (k in snapshot.children) {
                                val key = k.key
                                Log.d("key", k.toString())
                                val z = k.getValue(Term::class.java)
                                Log.d("z", z.toString())
                                if(titleText == key){
                                    detail.add(z!!)
                                }

                            }
                            val bAdapter = AdminTermAdapter(this@AdminTerms, detail)
                            termRecycler.visibility = View.VISIBLE
                            termRecycler.adapter = bAdapter
                            termRecycler.layoutManager = LinearLayoutManager(this@AdminTerms)
                            //LinearLayoutManager(requireContext())

                            // }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@AdminTerms, "A 2nd $error occured", Toast.LENGTH_LONG).show()

                        }

                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {
                // dialog!!.dismiss()
                Toast.makeText(this@AdminTerms, "An $error occured", Toast.LENGTH_LONG).show()
            }

        })
    }
}