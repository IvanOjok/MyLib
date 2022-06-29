package com.example.mylib.ui.admin

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.mylib.R
import com.example.mylib.model.Term
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog

class AdminAddTerms : AppCompatActivity() {

    private val CAPTURE_PERMISSION_CODE = 1000
    // private val IMAGE_CAPTURE_CODE = 1001
    val PICK_PERMISSION_CODE = 1002
    val IMAGE_PICK_CODE = 1003
    var image_uri: Uri? = null
    lateinit var tUrl: ImageView
    var mAuth: FirebaseAuth?= null
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_terms)

        val title = intent.getStringExtra("bTitle")
        val bClass = intent.getStringExtra("bookClass")
        val bUrl = intent.getStringExtra("bUrl")


        mAuth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()

        val tTitle = findViewById<EditText>(R.id.termTitle)
        val tPrice = findViewById<EditText>(R.id.termPrice)
        tUrl = findViewById<ImageView>(R.id.termUrl)
        val tLesssons = findViewById<EditText>(R.id.termLessons)
        val tTopics = findViewById<EditText>(R.id.termTopics)


        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener {

            val termTitle = tTitle.text.toString()
            val termPrice = tPrice.text.toString()
            val termLessons = tLesssons.text.toString()
            val termTopics = tTopics.text.toString()

            if (termTitle.isNotEmpty() && termPrice.isNotEmpty() && termLessons.isNotEmpty() && termTopics.isNotEmpty() && image_uri != null){
                dialog.show()
                uploadTerm(
                    termTitle,
                    termPrice,
                    termLessons,
                    termTopics,
                    title!!,
                    bUrl!!,
                    bClass!!,
                )
                Log.d("upload", "${uploadTerm(
                    termTitle,
                    termPrice,
                    termLessons,
                    termTopics,
                    title,
                    bUrl,
                    bClass,
                )}")
            }
            else{
                Toast.makeText(this, "fill in all details", Toast.LENGTH_SHORT).show()
            }

        }

        tUrl.setOnClickListener {
            pickImageFromGallery()
        }

    }

    fun pickImageFromGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions:Array<out String>, grantResults:IntArray) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        when (requestCode) {

            PICK_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery()
                } else {
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image_uri = data!!.data
            //img.setImageURI(image_uri)

            val bitmap = MediaStore.Images.Media
                .getBitmap(
                    contentResolver,
                    image_uri
                )
            tUrl.setImageBitmap(bitmap)
        }
    }


    // UploadImage method
    fun uploadTerm(
        termTitle:String,
        termPrice:String,
        lessons: String,
        topics: String,
        bookTitle:String,
        bookUrl: String,
        bookClass: String,
    ) {
        if (image_uri != null && mAuth!!.currentUser != null) {
            Log.d("image", image_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child("terms")
            val q =  sReference.putFile(image_uri!!)
            val utask = q.continueWithTask{ task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                else{
                    val t = sReference.downloadUrl
                    dbStore(
                        termTitle,
                        termPrice,
                        t.toString(),
                        lessons,
                        topics,
                        bookTitle,
                        bookUrl,
                        bookClass,
                    )
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore(
                        termTitle,
                        termPrice,
                        imgUrl,
                        lessons,
                        topics,
                        bookTitle,
                        bookUrl,
                        bookClass,
                    )
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun dbStore(termTitle: String,
                termPrice: String,
                termIcon:String,
                lessons: String,
                topics: String,
                bookTitle: String,
                bookUrl: String,
                bookClass:String
    ){
        val db =
            FirebaseDatabase.getInstance().getReference("/termDetails").child(termTitle)
                .setValue(
                    Term(
                        termTitle,
                        termPrice,
                        termIcon,
                        lessons,
                        topics,
                        bookTitle,
                        bookUrl,
                        bookClass,
                    )
                )
        if (db.isCanceled) {
            Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
                .show()
        } else {
            startActivity(Intent(this, AdminBooks::class.java))
            finish()
        }
    }

}