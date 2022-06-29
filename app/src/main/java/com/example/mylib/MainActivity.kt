package com.example.mylib


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mylib.latest.HomeActivity
import com.example.mylib.model.offline.AppDatabase
import com.example.mylib.model.PrefsManager
import com.google.firebase.FirebaseApp

class MainActivity : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dbase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbase = AppDatabase.getInstance(applicationContext)



        FirebaseApp.initializeApp(this)

        prefsManager.setContext(this.application)

        Handler().postDelayed({
            if (prefsManager.isProfileLoggedIn()){
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else
            if (prefsManager.isLoggedIn()){
                //finish()
                startActivity(Intent(this, ProfilesActivity::class.java))
                finish()
            }
            else{
                startActivity(Intent(this, SignUp::class.java))
                finish()
            }
//
        }, 3000)
    }
}