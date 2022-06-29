package com.example.mylib.model

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

enum class PrefsManager{
    INSTANCE;

    //constructor() : this()
    //private constructor()

    private var context: Application? = null

    constructor(application: Application) {
        this.context = application
    }

    constructor()

    fun onLogin(student: User): Boolean{
        val sharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(KEY_PHONE, student.phoneNo!!)
        editor.putString(KEY_EMAIL, student.email)
        editor.apply()
        return true
    }

    fun isLoggedIn():Boolean{
        val sharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(KEY_EMAIL, null) != null)
            return true
        return false
    }

    fun getUser(): User {
        val sharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return User(
            email = sharedPreferences.getString(KEY_EMAIL, null)!!,
            phoneNo = sharedPreferences.getString(KEY_PHONE, null)!!,

        )
    }

    fun onLoginProfile(profile: Profile): Boolean{
        val sharedPreferences = context!!.getSharedPreferences(P_PREF_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(P_ID, profile.id)
        editor.putString(P_NAME, profile.name)
        editor.putString(P_GENDER, profile.gender)
        editor.putString(P_CLASS, profile.stdClass)
        editor.apply()
        return true
    }

    fun isProfileLoggedIn():Boolean{
        val sharedPreferences = context!!.getSharedPreferences(P_PREF_NAME, Context.MODE_PRIVATE)
        if (sharedPreferences.getString(P_ID, null) != null)
            return true
        return false
    }


    fun getProfile(): Profile {
        val sharedPreferences = context!!.getSharedPreferences(P_PREF_NAME, Context.MODE_PRIVATE)
        return Profile(
            id = sharedPreferences.getString(P_ID, null)!!,
            name = sharedPreferences.getString(P_NAME, null)!!,
            gender = sharedPreferences.getString(P_GENDER, null)!!,
            stdClass = sharedPreferences.getString(P_CLASS, null)!!,
            )
    }

    fun onProfileLogout(): Boolean{
        val sharedPreferences = context!!.getSharedPreferences(P_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }

    fun onLogout(): Boolean{
        val sharedPreferences = context!!.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        return true
    }

    fun setContext(ctx: Application){
        context = ctx
    }
}