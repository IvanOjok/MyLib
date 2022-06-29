package com.example.mylib

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mylib.model.*
import com.example.mylib.model.offline.AppDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

 class ProfilesActivity : AppCompatActivity() {

     private val prefsManager = PrefsManager.INSTANCE
     lateinit var dbase: AppDatabase
     var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profiles)

        dbase = AppDatabase.getInstance(applicationContext)
        prefsManager.setContext(this.application)
        dialog = SpotsDialog.Builder().setContext(this).build()

//        val x = dbase.ProfileDao().getProfiles()
//        for (y in 0..x.size){
//            Log.d("y", "${x[y].name}")
//        }

        val recycler = findViewById<RecyclerView>(R.id.profiles)
        val list = dbase.ProfileDao().getProfiles()
        val listSize = list.size
        if (listSize > 0) {
            list.add(ProfileClass("tttt", "Add Profile", "Add", "none"))
            val adapter = ProfileAdapter(this, list)
            recycler.layoutManager = GridLayoutManager(this, 2)
            recycler.adapter = adapter
        }
        else{
        val p = ArrayList<Profile>()
        val profList = ArrayList<ProfileClass>()
        val phone = prefsManager.getUser().phoneNo
        FirebaseDatabase.getInstance().getReference("/profiles").child(phone!!).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dialog!!.dismiss()

                if (snapshot.exists()){
                    for (y in snapshot.children){
                        Log.d("y", "$y")
                        val z = y.getValue(Profile::class.java)
                        Log.d("z", "${z!!.name}")

                        val prof = ProfileClass(z.id!!, z.name!!, z.gender!!, z.stdClass!!)
                        profList.add(prof)
                        profList.add(ProfileClass("tttt", "Add Profile", "Add", "none"))
                        dbase.ProfileDao().insertProfile(prof)

                        p.add(z)
                        Log.d("p", "$p")
                    }

                    val adapterR = ProfileAdapter(this@ProfilesActivity, profList)
                    recycler.layoutManager = GridLayoutManager(this@ProfilesActivity, 2)
                    recycler.adapter = adapterR
                }
                else{
                    profList.add(ProfileClass("tttt", "Add Profile", "Add", "none"))

                    val adapterR = ProfileAdapter(this@ProfilesActivity, profList)
                    recycler.layoutManager = GridLayoutManager(this@ProfilesActivity, 2)
                    recycler.adapter = adapterR
                }



            }

            override fun onCancelled(error: DatabaseError) {
                dialog!!.dismiss()
                Toast.makeText(this@ProfilesActivity, "An $error occured", Toast.LENGTH_LONG).show()
            }

         })
        }

    }
}