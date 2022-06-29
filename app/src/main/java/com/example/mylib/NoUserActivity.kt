package com.example.mylib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class NoUserActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_user_account)

        val p = intent.getStringExtra("phone")

        val no = findViewById<TextView>(R.id.textView2)
        no.text = "The number $p isn't registered on our platform"

        val bt = findViewById<Button>(R.id.free)
        bt.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        val num = findViewById<TextView>(R.id.textView3)
        num.setOnClickListener {
            startActivity(Intent(this, MobileNumberActivity::class.java))
        }

    }
}