package com.example.mylib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.User
import com.example.mylib.ui.admin.Admin
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MobileNumberActivity : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mobile_number)

        prefsManager.setContext(this.application)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }


        val next = findViewById<Button>(R.id.next)

        val login = intent.getStringExtra("login")

        val mail = intent.getStringExtra("email")

        val phone = findViewById<EditText>(R.id.phone)

        if (login != null) {
            val text = findViewById<TextView>(R.id.textView)
            text.setText(R.string.login)
        }

        next.setOnClickListener {
            val tel = phone.text.toString()

            if (tel == "admin001"){
                startActivity(Intent(this, Admin::class.java))
            }
            else
            if (tel.isNotEmpty() && tel.length == 13) {

                val intent = Intent(this, VerificationActivity::class.java)

                if (login != null) {

                    val db = FirebaseDatabase.getInstance()

                    val d =
                        db.getReference().child("/users").child(tel).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    val user = snapshot.getValue(User::class.java)
                                    prefsManager.onLogin(user!!)

                                    intent.putExtra("login", "login")
                                    intent.putExtra("phone", tel)
                                    startActivity(intent)

                                }
                                else{
                                    val i = Intent(this@MobileNumberActivity, NoUserActivity::class.java)
                                    i.putExtra("phone", tel)
                                    startActivity(i)
                                }
                                
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@MobileNumberActivity, "An error occured $error", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                        else
                if (mail != null) {
                    val email = mail
                    intent.putExtra("email", email)
                    intent.putExtra("phone", tel)
                    startActivity(intent)
                }

            } else {
                Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show()
            }
        }
    }
}