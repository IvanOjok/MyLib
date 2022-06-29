package com.example.mylib

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.mylib.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    val G_SIGN_IN = 1000
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var db:FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val login = findViewById<Button>(R.id.login)
        login.setOnClickListener {
            val intent = Intent(this, MobileNumberActivity::class.java)
            intent.putExtra("login", "login")
            startActivity(intent)
        }

        val sign = findViewById<Button>(R.id.free)
        sign.setOnClickListener {
            startActivity(Intent(this, EmailActivity::class.java))
        }

//        val google = findViewById<Button>(R.id.google)
//
//        val gso = GoogleSignInOptions
//            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//
//        google.setOnClickListener {
//            signIn()
//        }

    }

//    fun signIn(){
//        val i = googleSignInClient.signInIntent
//        startActivityForResult(i, G_SIGN_IN)
//
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == G_SIGN_IN){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)
//                firebaseAuthWithGoogle(account)
//            }
//            catch (e:ApiException){
//                Log.w("TAG", "signInWithCredential:failure", e)
//            }
//        }
//    }
//
//
//    fun firebaseAuthWithGoogle(acc: GoogleSignInAccount) {
//        val credential = GoogleAuthProvider.getCredential(acc.idToken, null)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success")
//                    val user = auth.currentUser
//                    var displayName = user!!.displayName
//                    var phoneNumber:String ?= user.phoneNumber
//                    var email:String ?= user.email
//                    var uid:String = user.uid
//
//                    if (displayName != null && phoneNumber != null && email != null && uid != null){
//                        db = FirebaseDatabase.getInstance()
//                        val ref = db.getReference().child("/users").child(uid).setValue(User(uid, displayName, phoneNumber, email))
//
//                    }else{
//                        if (displayName == null){
//                            displayName = "BB"
//                        }
//                        if (phoneNumber == null){
//                            phoneNumber = "96"
//                        }
//                        if(email == null){
//                            email = "email"
//                        }
//                        if (uid == null){
//                            uid = "000"
//                        }
//
//                        db = FirebaseDatabase.getInstance()
//                        val ref = db.getReference().child("/users").child(uid).setValue(User(uid, displayName, phoneNumber, email))
//
//                    }
//
//                    //updateUI(user)
//                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w("TAG", "signInWithCredential:failure", task.exception)
//                    //updateUI(null)
//
//                    Toast.makeText(this, "Sign In Failed", Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        if (GoogleSignIn.getLastSignedInAccount(this) != null){
//            startActivity(Intent(this, Home::class.java))
//        }
//    }
}