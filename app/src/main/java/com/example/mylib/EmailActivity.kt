package com.example.mylib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class EmailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        val next = findViewById<Button>(R.id.next)


        next.setOnClickListener {

        val email = findViewById<EditText>(R.id.email)
        val mail = email.text.toString()

        val intent = Intent(this, MobileNumberActivity::class.java)
        intent.putExtra("email", mail)
        startActivity(intent)


        }
    }
}