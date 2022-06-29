package com.example.mylib.ui.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.admin.AdminBookAdapter
import com.example.mylib.model.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminBooks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_books)

        val bClass = intent.getStringExtra("class")
        val bookRecycler = findViewById<RecyclerView>(R.id.bookRecycler)


        if(bClass != null){

         val x = ArrayList<Book>()
        FirebaseDatabase.getInstance().getReference("/book").orderByChild("bookClass").equalTo(bClass).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){

                Log.d("snap", "$snapshot")
                for (k in snapshot.children){
                    val key = k.key
                    Log.d("y", "$k")
                    Log.d("key", "$key")
                    val z = k.getValue(Book::class.java)
                    Log.d("z", "$z")
                    Log.d("z", "${z!!.bookSubject}")
                    Log.d("z", "${z.bookClass}")

//                    bookTitle = z.bookTitle
//                    bookTerm = "Term 1, Term 2, Term 3"
//                    bookPrice = z.bookPrice
//                    bookUrl = z.bookUrl

                        x.add(z)
                        Log.d("Added book", "${z}")
                        //Log.d("p", "$book")
                    }

                val bAdapter = AdminBookAdapter(this@AdminBooks, x)
                bookRecycler.adapter = bAdapter
                bookRecycler.layoutManager = LinearLayoutManager(this@AdminBooks)


            }
                else{
                    Toast.makeText(this@AdminBooks, "No books", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminBooks, "An $error occurred", Toast.LENGTH_LONG).show()

            }

        })


        }
        else{
            Toast.makeText(this@AdminBooks, "An empty class", Toast.LENGTH_LONG).show()
        }







        val addBook = findViewById<ImageView>(R.id.addBook)
        addBook.setOnClickListener {
            val intent = Intent(this, AdminAddBooks::class.java)
            intent.putExtra("bClass", bClass)
            startActivity(intent)
        }
    }
}