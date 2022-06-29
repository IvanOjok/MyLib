package com.example.mylib.ui.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.*
import com.example.mylib.R
import com.example.mylib.model.Lesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import dmax.dialog.SpotsDialog

class AdminAddLessons : AppCompatActivity() {

    private val CAPTURE_PERMISSION_CODE = 1000
    // private val IMAGE_CAPTURE_CODE = 1001
    val PICK_PERMISSION_CODE = 1002
    val IMAGE_PICK_CODE = 1003
    val PICK_VIDEO_PERMISSION_CODE = 1004
    val VIDEO_PICK_CODE = 1005
    var image_uri: Uri? = null
    var video_uri: Uri? = null
    lateinit var lessonUrl: ImageView
    var mAuth: FirebaseAuth?= null
    lateinit var dialog: android.app.AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_lessons)

        val bTitle = intent.getStringExtra("bTitle")
        val bTopic = intent.getStringExtra("bTopic")
        val bTerm = intent.getStringExtra("bTerm")
        val bClass = intent.getStringExtra("bClass")

        val type = arrayOf<String?>("video", "image")

        val tAdapter = ArrayAdapter<Any?>(this, R.layout.list_items, type)

        val tEditTextFilledExposedDropdown: AutoCompleteTextView = findViewById(R.id.mediaType)
        tEditTextFilledExposedDropdown.setAdapter(tAdapter)

        tEditTextFilledExposedDropdown.setOnClickListener {
            (it as AutoCompleteTextView).showDropDown()
        }

        mAuth = FirebaseAuth.getInstance()
        dialog = SpotsDialog.Builder().setContext(this).build()

        val lTitle = findViewById<EditText>(R.id.lessonTitle)
        val lContent = findViewById<EditText>(R.id.lessonContent)
        lessonUrl = findViewById<ImageView>(R.id.lessonUrl)
        val mCaption = findViewById<EditText>(R.id.mediaCaption)



        val save = findViewById<Button>(R.id.save)
        save.setOnClickListener {

            val lessonTitle = lTitle.text.toString()
            val lessonContent = lContent.text.toString()
            val mediaCaption = mCaption.text.toString()
            val mediaType = tEditTextFilledExposedDropdown.text.toString()

            if (lessonTitle.isNotEmpty() && lessonContent.isNotEmpty() && mediaCaption.isNotEmpty() && mediaType.isNotEmpty()){
                dialog.show()
               uploadLesson(
                lessonTitle,
                lessonContent,
                mediaCaption,
                mediaType,
                bTopic!!,
                bTerm!! ,
                bTitle!!,
                bClass!!,
               )
                Log.d("upload", "${
                   uploadLesson(
                    lessonTitle,
                    lessonContent,
                    mediaCaption,
                    mediaType,
                    bTopic,
                    bTerm,
                    bTitle,
                    bClass,
                )}")
            }
            else{
                Toast.makeText(this, "fill in all details", Toast.LENGTH_SHORT).show()
            }

        }

        lessonUrl.setOnClickListener {
            val mediaType = tEditTextFilledExposedDropdown.text.toString()

            if (mediaType == "image"){
                pickImageFromGallery()
            }
            else if (mediaType == "video"){
                pickVideoFromGallery()
            }
            else{
                Toast.makeText(this, "Select media Type", Toast.LENGTH_LONG).show()
            }

        }

    }

    fun pickImageFromGallery(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICK_CODE)
    }

    fun pickVideoFromGallery(){
        val intent = Intent()
        intent.type = "video/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), VIDEO_PICK_CODE)
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

            PICK_VIDEO_PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickVideoFromGallery()
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
            lessonUrl.setImageBitmap(bitmap)
        }
        else
            if(resultCode == Activity.RESULT_OK && requestCode == VIDEO_PICK_CODE) {
                video_uri = data!!.data
                //img.setImageURI(image_uri)

//                val bitmap = MediaStore.Images.Media
//                    .getBitmap(
//                        contentResolver,
//                        image_uri
//                    )
//                lessonUrl.setImageBitmap(bitmap)
            }
    }


    // UploadImage method
    fun uploadLesson(
         LessonTitle: String,
         lessonContent:String,
         mediaCaption: String,
         mediaType:String,
         topicTitle:String,
         termTitle:String ,
         bookTitle: String,
         bookClass: String,
    ) {
        if (image_uri != null && mAuth!!.currentUser != null) {
            Log.d("image", image_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/images").child("lessons")
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
                        LessonTitle,
                        lessonContent,
                        t.toString(),
                        mediaCaption,
                        mediaType,
                        topicTitle,
                        termTitle,
                        bookTitle,
                        bookClass,
                    )
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore(
                        LessonTitle,
                        lessonContent,
                        imgUrl,
                        mediaCaption,
                        mediaType,
                        topicTitle,
                        termTitle,
                        bookTitle,
                        bookClass,
                    )
                }
                else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else
        if (video_uri != null && mAuth!!.currentUser != null) {
            Log.d("video", video_uri.toString())

            //// && mAuth!!.currentUser != null

            // Defining the child of storageReference
            val sReference = FirebaseStorage.getInstance().getReference().child("/videos").child("lessons")
            val q =  sReference.putFile(video_uri!!)
            val utask = q.continueWithTask{ task ->
                if (!task.isSuccessful){
                    task.exception.let {
                        throw it!!
                    }
                }
                else{
                    val t = sReference.downloadUrl
                    dbStore(
                        LessonTitle,
                        lessonContent,
                        t.toString(),
                        mediaCaption,
                        mediaType,
                        topicTitle,
                        termTitle,
                        bookTitle,
                        bookClass,
                    )
                }
                sReference.downloadUrl

            }.addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val imgUrl = task.result.toString()
                    dbStore(
                        LessonTitle,
                        lessonContent,
                        imgUrl,
                        mediaCaption,
                        mediaType,
                        topicTitle,
                        termTitle,
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

//    fun getVideoType(videoUri: Uri): String{
//        val r = contentResolver
//        val mineTypeMap = MimeTypeMap.getSingleton()
//        return mineTypeMap.getExtensionFromMimeType(r.getType(videoUri))!!
//    }

    fun dbStore(LessonTitle: String,
                lessonContent:String,
                lessonMedia:String,
                mediaCaption: String,
                mediaType:String,
                topicTitle:String,
                termTitle:String,
                bookTitle:String,
                bookClass:String,
                ){
        val db =
            FirebaseDatabase.getInstance().getReference("/lessons").child(termTitle).child(bookTitle).child(topicTitle).push()
                .setValue(
                    Lesson(
                       LessonTitle,
                       lessonContent,
                        lessonMedia,
                       mediaCaption,
                       mediaType,
                       topicTitle,
                       termTitle,
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