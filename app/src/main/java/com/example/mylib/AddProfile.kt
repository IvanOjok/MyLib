package com.example.mylib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.mylib.model.offline.AppDatabase
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.ProfileClass
import com.google.firebase.database.FirebaseDatabase

class AddProfile : AppCompatActivity() {

    private val prefsManager = PrefsManager.INSTANCE
    lateinit var dbase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_profile)

        dbase = AppDatabase.getInstance(applicationContext)
        prefsManager.setContext(this.application)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, ProfilesActivity::class.java))
        }


        val termD = arrayOf<String?>("Primary 1", "Primary 2", "Primary 3", "Primary 4", "Primary 5", "Primary 6", "Primary 7")

        val tadapter = ArrayAdapter<Any?>(this, R.layout.list_items, termD)

        val teditTextFilledExposedDropdown: AutoCompleteTextView = findViewById(R.id.stdClass)
        teditTextFilledExposedDropdown.setAdapter(tadapter)

        teditTextFilledExposedDropdown.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }



        val named = findViewById<EditText>(R.id.name)
        val gendered = findViewById<ImageView>(R.id.imageView)
        val nxt = findViewById<Button>(R.id.next)

       // gendered.setOnClickListener {
        ///    gendered.setImageResource(R.)
       // }

        nxt.setOnClickListener {
            val name = named.text.toString()
           // val gender = gendered.drawable


            val term = teditTextFilledExposedDropdown.text.toString()

            if (name.isNotEmpty() && term.isNotEmpty()){
                val phone = prefsManager.getUser().phoneNo
                val db = FirebaseDatabase.getInstance().getReference("/profiles")
                val ref = db.child(phone!!).push()
                val key = ref.key
                val profile = ProfileClass(key!!, name, "M" ,term)
                val set = ref.setValue(profile)

                if (set.isCanceled){
                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show()
                }
                else{
                    dbase.ProfileDao().insertProfile(profile)
                    startActivity(Intent(this, Home::class.java))
                }
            } 
            else{
                Toast.makeText(this, "Enter all credentials", Toast.LENGTH_SHORT).show()
            }
        }

    }
}