package com.example.mylib.ui.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.R
import com.example.mylib.adapters.admin.AdminClassAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Admin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val classRecycler = findViewById<RecyclerView>(R.id.classRecycler)
        val p = ArrayList<String>()
        val dbRef =
            FirebaseDatabase.getInstance().getReference("/classes").addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (y in snapshot.children) {
                        val key = y.key
                        Log.d("y", "$y")
                        val z = y.getValue(String()::class.java)
                        Log.d("z", "${z}")
                        p.add(z!!)
                        Log.d("p", "$p")
                    }

                    val adapter = AdminClassAdapter(this@Admin, p)
                    classRecycler.adapter = adapter
                    classRecycler.layoutManager = LinearLayoutManager(this@Admin)

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

        val addClass = findViewById<ImageView>(R.id.addClass)
        addClass.setOnClickListener {

        }
    }
}