package com.example.mylib.model

import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM ProfileClass")
    fun getProfiles():MutableList<ProfileClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(vararg profile: ProfileClass)

    @Delete
    fun deleteProfile(profile: ProfileClass)

    @Update
    fun updateProfile(vararg profile: ProfileClass)
}

@Dao
interface BookDao {
    @Query("SELECT * FROM BookClass")
    fun getBooks():MutableList<BookClass>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(vararg book: BookClass)

    @Delete
    fun deleteBooks(book: BookClass)

    @Update
    fun updateBook(vararg book: BookClass)
}




@Dao
interface LessonDao {
    @Query("SELECT * FROM LessonClass")
    fun getLessons():MutableList<LessonClass>

    @Insert
    fun insertLesson(vararg lesson: LessonClass)

    @Delete
    fun deleteLesson(lesson: LessonClass)

    @Update
    fun updateLesson(vararg lesson: LessonClass)
}