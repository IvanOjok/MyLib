package com.example.mylib

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.mylib.model.PrefsManager
import com.example.mylib.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import dmax.dialog.SpotsDialog
import java.util.concurrent.TimeUnit

class VerificationActivity : AppCompatActivity() {

    var mVerificationId: String ?= null
    var dialog: AlertDialog? = null

    private val prefsManager = PrefsManager.INSTANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        prefsManager.setContext(this.application)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }

        dialog = SpotsDialog.Builder().setContext(this).build()

        val next = findViewById<Button>(R.id.verify)
        val code = findViewById<EditText>(R.id.code)

        val login = intent.getStringExtra("login")

        val mail = intent.getStringExtra("email")

        val phone = intent.getStringExtra("phone")

        val tel = findViewById<TextView>(R.id.phone)
        tel.setText("We have sent a 6-digit code by sms to \\n $phone")

        if (login != null) {
            val text = findViewById<TextView>(R.id.textView)
            text.setText(R.string.login)
        }

        sendVerificationCode(phone!!)

        next.setOnClickListener {
            val otp = code.text.toString()
            if (otp.isEmpty() || otp.length < 6) {
                return@setOnClickListener
            }
            verifyVerificationCode(otp)
        }
    }

    fun sendVerificationCode(mobile: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mobile,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )
    }

    //the callback to detect the verification status
    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                s: String,
                forceResendingToken: PhoneAuthProvider.ForceResendingToken,
            ) {
                // super.onCodeSent(s, forceResendingToken)

                //storing the verification id that is sent to the user
                mVerificationId = s
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode


                //Log.d("code", code!!)
                //sometime the code is not detected automatically
                //in this case the code will be null
                //so user has to manually enter the code
                if (code != null) {
                    //code = firstPinView.text
                    //verifying the code
                    verifyVerificationCode(code)
                }

            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(this@VerificationActivity, "Wrong Phone number format", Toast.LENGTH_LONG).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(this@VerificationActivity, "Quota reached", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this@VerificationActivity, e.message, Toast.LENGTH_LONG).show()
                }

                startActivity(Intent(this@VerificationActivity, MobileNumberActivity::class.java))
                finish()
            }

        }

    fun verifyVerificationCode(code: String) {
        //creating the credential
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, code)

        //signing the user
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this@VerificationActivity,
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        dialog!!.dismiss()
                        val phoneNumber = intent.getStringExtra("phone")

                        val login = intent.getStringExtra("login")
                        val mail = intent.getStringExtra("email")

                        val intent = Intent(this, ProfilesActivity::class.java)
                        if (login != null) {
                            startActivity(intent)
                        }
                        else
                            if (mail != null) {
                                val email = mail

                                Log.d("email", email)
                                Log.d("phone", phoneNumber!!)
                               val db = FirebaseDatabase.getInstance()
                                val user = User(phoneNumber, email)
                        val ref = db.getReference("/users").child(phoneNumber).setValue(user)

                                if (ref.isCanceled){
                                    Toast.makeText(this, "error occurred", Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    prefsManager.onLogin(user)
                                    startActivity(intent)
                                }

                            }

                    } else {
                        dialog!!.dismiss()
                        Toast.makeText(this@VerificationActivity, task.exception.toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
    }



}