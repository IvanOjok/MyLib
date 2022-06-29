package com.example.mylib.latest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.mylib.R
import com.example.mylib.ui.search.DashboardFragment
import com.example.mylib.ui.home.HomeFragment
import com.example.mylib.ui.library.NotificationsFragment
import com.example.mylib.ui.store.StoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home2)

        setFragment(HomeFragment())

        val navigation = findViewById<BottomNavigationView>(R.id.nav_view)

        navigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_home -> setFragment(HomeFragment())
                R.id.navigation_dashboard -> setFragment(DashboardFragment())
                R.id.navigation_notifications -> setFragment(NotificationsFragment())
                R.id.navigation_store -> setFragment(StoreFragment())
            }
            true
        }
    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_container, fragment)
                .commit()
        }
    }
}