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
import com.example.mylib.model.Topic
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog

class AdminAddTopics : AppCompatActivity() {

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
        setContentView(R.layout.activity_admin_add_topics)

        val bTitle = intent.getStringExtra("bTitle")
        val bClass = intent.getStringExtra("bClass")
        val bTerm = intent.getStringExtra("bTerm")

        mAuth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()

        val tTitle = findViewById<EditText>(R.id.topicTitle)
        val tTheme = findViewById<EditText>(R.id.topicTheme)
        tUrl = findViewById<ImageView>(R.id.topicUrl)


        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener {

            val topicTitle = tTitle.text.toString()
            val topicTheme = tTheme.text.toString()

            if (topicTitle.isNotEmpty() && topicTheme.isNotEmpty() && image_uri != null){
                dialog.show()
                uploadTopic(
                    bTerm!! ,
                    topicTheme,
                    topicTitle,
                    bTitle!!,
                    bClass!!,
                )
                Log.d("upload", "${uploadTopic(
                    bTerm ,
                    topicTheme,
                    topicTitle,
                    bTitle,
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
    fun uploadTopic(
        termTitle:String ,
        topicTheme: String,
        topicTitle:String,
        bookTitle: String,
        bookClass: String,
    ) {
        if (image_uri != null && mAuth!!.currentUser != null) {
            Log.d("image", image_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child("topics")
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
                        t.toString(),
                        topicTheme,
                        topicTitle,
                        bookTitle,
                        bookClass,
                    )
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore(
                        termTitle,
                        imgUrl,
                        topicTheme,
                        topicTitle,
                        bookTitle,
                        bookClass,
                    )
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    fun dbStore(termTitle:String ,
                topicIcon:String ,
                topicTheme: String,
                topicTitle:String,
                bookTitle: String,
                bookClass: String,


    ){
        val db =
            FirebaseDatabase.getInstance().getReference("/topics").child(termTitle).child(bookTitle).child(topicTitle)
                .setValue(
                    Topic(
                        termTitle,
                        topicIcon,
                        topicTheme,
                        topicTitle,
                        bookTitle,
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