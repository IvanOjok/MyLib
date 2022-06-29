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
import com.example.mylib.model.Book
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog

class AdminAddBooks : AppCompatActivity() {
    //private val prefsManager = PrefsManager.INSTANCE

    private val CAPTURE_PERMISSION_CODE = 1000
    // private val IMAGE_CAPTURE_CODE = 1001
    val PICK_PERMISSION_CODE = 1002
    val IMAGE_PICK_CODE = 1003

    var image_uri: Uri? = null
    lateinit var bUrl:ImageView
    var mAuth: FirebaseAuth?= null
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_books)

        mAuth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()

        val bookClass = intent.getStringExtra("bClass")

        val bTitle = findViewById<EditText>(R.id.bookTitle)
        val bPrice = findViewById<EditText>(R.id.bookPrice)
        val bDesc = findViewById<EditText>(R.id.bookDesc)
        val bColor = findViewById<EditText>(R.id.bookColor)
        bUrl = findViewById<ImageView>(R.id.bookUrl)
        val bSubject = findViewById<EditText>(R.id.bookSubject)
        val bLesssons = findViewById<EditText>(R.id.bookLessons)
        val bTopics = findViewById<EditText>(R.id.bookTopics)



        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener {

            val bookTitle = bTitle.text.toString()
            val bookPrice = bPrice.text.toString()
            val bookDescription = bDesc.text.toString()
            val bookColor = bColor.text.toString()
            //val bookUrl = bUrl.text.toString()
            val bookSubject = bSubject.text.toString()
            val bookLessons = bLesssons.text.toString()
            val bookTopics = bTopics.text.toString()



            if (bookTitle.isNotEmpty() && bookPrice.isNotEmpty() && bookDescription.isNotEmpty() && bookColor.isNotEmpty() && bookSubject.isNotEmpty() && bookLessons.isNotEmpty() && bookTopics.isNotEmpty() && image_uri != null){
                dialog.show()
                uploadBook(
                    bookTitle,
                    bookPrice,
                    bookDescription,
                    bookColor,
                    bookSubject,
                    bookClass!!,
                    bookLessons,
                    bookTopics,
                )
                Log.d("upload", "${uploadBook(
                    bookTitle,
                    bookPrice,
                    bookDescription,
                    bookColor,
                    bookSubject,
                    bookClass,
                    bookLessons,
                    bookTopics,
                )}")
            }
            else{
                Toast.makeText(this, "fill in all details", Toast.LENGTH_SHORT).show()
            }

        }

        bUrl.setOnClickListener {
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
            bUrl.setImageBitmap(bitmap)
        }
    }


    // UploadImage method
    fun uploadBook(
        bookTitle: String,
        bookPrice: String,
        bookDescription:String,
        bookColor: String,
        bookSubject: String,
        bookClass: String,
        lessons: String,
        topics: String,
    ) {
        if (image_uri != null && mAuth!!.currentUser != null) {
            Log.d("image", image_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child("books")
            val q =  sReference.putFile(image_uri!!)
            val utask = q.continueWithTask{ task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                else{
                    val t = sReference.downloadUrl
                    dbStore(bookTitle,
                        bookPrice,
                        bookDescription,
                        bookColor,
                        t.toString(),
                        bookSubject,
                        bookClass,
                        lessons,
                        topics,)
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore(bookTitle,
                        bookPrice,
                        bookDescription,
                        bookColor,
                        imgUrl,
                        bookSubject,
                        bookClass,
                        lessons,
                        topics,)
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
//        else{
//
//            Log.d("image", image_uri.toString())
//
//            Log.d("anonymous", "ddd")
//            signInAnonymously(id,
//                firstName,
//                lastName,
//                email,
//                profession,
//                doctorNo,
//                phone)
//
//        }
//    }
//
//    private fun signInAnonymously( id:String,
//                                   firstName:String,
//                                   lastName:String,
//                                   email:String,
//                                   profession:String,
//                                   doctorNo:String,
//                                   phone:String) {
//
//        mAuth!!.signInAnonymously().addOnSuccessListener(this, OnSuccessListener<AuthResult?> {
//            // Defining the child of storageReference
//            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child(image_uri.toString())
//            val q = sReference.putFile(image_uri!!)
//
//            val utask = q.continueWithTask{ task ->
//                if (!task.isSuccessful){
//                    task.exception.let {
//                        throw it!!
//                    }
//                }
//                else{
//                    val t = sReference.downloadUrl
//                    dbStore( id,
//                        firstName,
//                        lastName,
//                        email,
//                        profession,
//                        doctorNo,
//                        t.toString(),
//                        phone)
//                }
//                sReference.downloadUrl
//
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful){
//                    val imgUrl = task.result.toString()
//                    dbStore( id,
//                        firstName,
//                        lastName,
//                        email,
//                        profession,
//                        doctorNo,
//                        imgUrl,
//                        phone)
//                }
//                else{
//                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//                }
//            }


//        })
//            .addOnFailureListener(this,
//                OnFailureListener { exception ->
//                    Log.e(
//                        "STORage",
//                        "signInAnonymously:FAILURE",
//                        exception
//                    )
//                })

    }

    fun dbStore(bookTitle: String,
                bookPrice: String,
                bookDescription:String,
                bookColor: String,
                bookUrl: String,
                bookSubject: String,
                bookClass: String,
                lessons: String,
                topics: String,){
        val db =
            FirebaseDatabase.getInstance().getReference("/book").child(bookTitle)
                .setValue(
                    Book(
                        bookTitle,
                        bookPrice,
                        bookDescription,
                        bookColor,
                        bookUrl,
                        bookSubject,
                        bookClass,
                        lessons,
                        topics,
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