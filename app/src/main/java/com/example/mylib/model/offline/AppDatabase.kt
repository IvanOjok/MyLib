package com.example.mylib.model.offline

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mylib.model.BookClass
import com.example.mylib.model.BookDao
import com.example.mylib.model.ProfileClass
import com.example.mylib.model.ProfileDao

@Database(entities = arrayOf(ProfileClass::class, BookClass::class), version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun ProfileDao(): ProfileDao
    abstract fun BookDao(): BookDao

    companion object {
        var Instance: AppDatabase?= null
        fun getInstance(context: Context): AppDatabase {
            if (Instance == null){
                Instance = Room.databaseBuilder(context, AppDatabase::class.java, "roomdb").allowMainThreadQueries().build()
            }
            return Instance as AppDatabase
        }
    }
}
