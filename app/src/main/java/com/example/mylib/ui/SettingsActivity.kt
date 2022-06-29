package com.example.mylib.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.mylib.ProfilesActivity
import com.example.mylib.R
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.PrefsManager

class SettingsActivity : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val name = findViewById<TextView>(R.id.name)
        name.text = prefsManager.getProfile().name

        val edit = findViewById<TextView>(R.id.edit)
        edit.setOnClickListener {
            startActivity(Intent(this, EditProfile::class.java))
        }

        val logout = findViewById<Button>(R.id.logout)
        logout.setOnClickListener {
            prefsManager.onProfileLogout()
            startActivity(Intent(this, ProfilesActivity::class.java))
            finish()
        }

        val cancel = findViewById<ImageView>(R.id.cancel)
        cancel.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}