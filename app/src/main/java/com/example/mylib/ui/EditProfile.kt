package com.example.mylib.ui

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mylib.ProfilesActivity
import com.example.mylib.R
import com.example.mylib.model.offline.AppDatabase
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.Profile
import com.example.mylib.model.ProfileClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dmax.dialog.SpotsDialog

class EditProfile : AppCompatActivity() {
    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dbase: AppDatabase
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        dbase = AppDatabase.getInstance(applicationContext)
        dialog = SpotsDialog.Builder().setContext(this).build()


        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }

        val phone = prefsManager.getUser().phoneNo
        val profileName = prefsManager.getProfile().name
        val profileClass = prefsManager.getProfile().stdClass
        val profGender = prefsManager.getProfile().gender
        val name = findViewById<EditText>(R.id.name)
        name.setText(profileName)

        val teditTextFilledExposedDropdown: AutoCompleteTextView = findViewById(R.id.stdClass)
        teditTextFilledExposedDropdown.setText(profileClass)

        val sClass = arrayOf<String?>(
            "Primary 1",
            "Primary 2",
            "Primary 3",
            "Primary 4",
            "Primary 5",
            "Primary 6",
            "Primary 7"
        )

        val cAdapter = ArrayAdapter<Any?>(this, R.layout.list_items, sClass)


        teditTextFilledExposedDropdown.setAdapter(cAdapter)

        teditTextFilledExposedDropdown.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }


        val delete = findViewById<Button>(R.id.delete)
        val save = findViewById<Button>(R.id.save)

        save.setOnClickListener {
            dialog!!.show()
            val profName = name.text.toString()
            val studClass = teditTextFilledExposedDropdown.text.toString()

            if (profName.isNotEmpty() && studClass.isNotEmpty()) {

                val db = FirebaseDatabase.getInstance().getReference("/profiles").child(phone!!)
                    //.orderByChild("name").equalTo(profileName)
                db.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (i in snapshot.children) {
                            val key = i.key
                            val z = i.getValue(Profile::class.java)
                            if (z!!.name == profileName){

                                Log.d("i", key!!)
                                Log.d("z", "${z.name}")

                            FirebaseDatabase.getInstance().getReference("/profiles").child(phone)
                                .child(key)
                                .setValue(Profile(key, profName, profGender!!, studClass))
                            dbase.ProfileDao()
                                .updateProfile(ProfileClass(key, profName, profGender, studClass))

                                dialog!!.dismiss()
                            startActivity(Intent(this@EditProfile, ProfilesActivity::class.java))
                            finish()

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        dialog!!.dismiss()
                        Toast.makeText(this@EditProfile, "An error $error occurred", Toast.LENGTH_LONG)
                            .show()
                    }


                })
            }
        }


        delete.setOnClickListener {
            dialog!!.show()
            FirebaseDatabase.getInstance().getReference("/profiles").child(phone!!)
                //.orderByChild("name").equalTo(profileName)
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        for (i in snapshot.children) {

                            val key = i.key
                            val z = i.getValue(Profile::class.java)
                            if (z!!.name == profileName) {

                                Log.d("i", key!!)
                                Log.d("z", "${z.name}")
                                FirebaseDatabase.getInstance().getReference("/profiles")
                                    .child(phone)
                                    .child(key).removeValue()
                                dbase.ProfileDao().deleteProfile(
                                    ProfileClass(
                                        key,
                                        profileName!!,
                                        profGender!!,
                                        profileClass!!
                                    )
                                )

                                prefsManager.onProfileLogout()

                                dialog!!.dismiss()
                                startActivity(
                                    Intent(
                                        this@EditProfile,
                                        ProfilesActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        dialog!!.dismiss()
                        Toast.makeText(this@EditProfile, "An error $error occurred", Toast.LENGTH_LONG)
                            .show()
                    }

                })
        }



    }
}