package com.example.mylib

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com .example.mylib.databinding.ActivityHomeBinding
import com.example.mylib.ui.SettingsActivity
import com.google.android.material.appbar.MaterialToolbar

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val bar = findViewById<MaterialToolbar>(R.id.bar)
//        //setupActionBarWithNavController()
//        setSupportActionBar(bar)


//        val inflater = this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val vParent = findViewById<ViewGroup>(R.id.container)
//        val view = inflater.inflate(R.layout.home_header, vParent, false)
//
//        vParent.addView(view)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        //  setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        val name = intent.getStringExtra("name")
        val v = binding.root.findViewById<TextView>(R.id.textView2)
        Log.d("name", name!!)
        v.text = "You are welcome \n $name"

        val s = binding.root.findViewById<ImageView>(R.id.settings)
        s.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}