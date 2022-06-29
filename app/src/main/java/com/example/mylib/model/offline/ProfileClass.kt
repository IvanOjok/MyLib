 package com.example.mylib.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfileClass(
    @PrimaryKey(autoGenerate = false)
    var id:String,

    @ColumnInfo(name = "name")
    var name:String,

    @ColumnInfo(name = "gender")
    var gender:String,

    @ColumnInfo(name = "stdClass")
    var stdClass:String,
)

 @Entity
 data class BookClass(
     @PrimaryKey(autoGenerate = false)
     var dId:String,
     @ColumnInfo(name = "id")
     var id:String,
     @ColumnInfo(name = "bookTitle")
     var bookTitle: String,
     @ColumnInfo(name = "bookPrice")
     var bookPrice: String,
     @ColumnInfo(name = "bookDescription")
     var bookDescription: String,
     @ColumnInfo(name = "bookColor")
     var bookColor: String,
     @ColumnInfo(name = "bookUrl")
     var bookUrl: String,
     @ColumnInfo(name = "bookSubject")
     var bookSubject: String,
     @ColumnInfo(name = "bookClass")
     var bookClass: String,
     @ColumnInfo(name = "lessons")
     var lessons: String,
     @ColumnInfo(name = "topics")
     var topics: String,
 )

@Entity
data class LessonClass(
    @PrimaryKey(autoGenerate = true)
    var id:String,

    @ColumnInfo(name = "lessonTitle")
    var lessonTitle:String,

    @ColumnInfo(name = "lessonContent")
    var lessonContent:String,

    @ColumnInfo(name = "lessonMedia")
    var lessonMedia:String,

    @ColumnInfo(name = "mediaCaption")
    var mediaCaption:String,

    @ColumnInfo(name = "mediaType")
    var mediaType:String,

    @ColumnInfo(name = "topicTitle")
    var topicTitle:String,

    @ColumnInfo(name = "termTitle")
    var termTitle:String,

    @ColumnInfo(name = "bookTitle")
    var bookTitle:String,

    @ColumnInfo(name = "bookClass")
    var bookClass:String,

)

