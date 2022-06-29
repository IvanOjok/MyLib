package com.example.mylib.model

import android.icu.text.CaseMap

//data class Classes(
//    var classes:String ?= null,
//)
//
//data class Books (
//    var bookClass:ArrayList<BookDetail> ?= null,
//)

data class Book(
    var bookTitle: String? = null,
    var bookPrice: String? = null,
    var bookDescription: String? = null,
    var bookColor: String? = null,
    var bookUrl: String? = null,
    var bookSubject: String? = null,
    var bookClass: String? = null,
    var lessons: String? = null,
    var topics: String? = null,
)

data class Term(
    var termTitle:String ?= null,
    var termPrice:String ?= null,
    var termIcon:String ?= null,
    var lessons: String? = null,
    var topics: String? = null,
    var bookTitle:String ?= null,
    var bookUrl: String? = null,
    var bookClass: String? = null,
)

data class Topic(
    var termTitle:String ?= null,
    var topicIcon:String ?= null,
    var topicTheme: String? = null,
    var topicTitle:String ?= null,
    var bookTitle: String? = null,
    var bookClass: String? = null,
)

data class Lesson(
    var lessonTitle: String? = null,
    var lessonContent:String ?= null,
    var lessonMedia:String ?= null,
    var mediaCaption: String? = null,
    var mediaType:String ?= null,
    var topicTitle:String ?= null,
    var termTitle:String ?= null,
    var bookTitle: String? = null,
    var bookClass: String? = null,
)


data class BookLesson(
    var key:String ?= null,
    var lesson:Lesson ?= null
)

data class BookList(
    var key:String ?= null,
    var lesson:Book ?= null
)



//
//class BookDetail {
//    var bookTitle: String? = null
//    var bookPrice: String? = null
//    var bookDescription: String? = null
//    var bookColor: String? = null
//    var bookUrl: String? = null
//    var bookSubject: String? = null
//    var bookClass: String? = null
//    var terms: Terms ?= null
//
//    constructor(bookTitle: String, bookPrice: String, bookDescription:String, bookColor:String, bookUrl:String, bookSubject:String, bookClass: String, terms: Terms){
//        this.bookTitle = bookTitle
//        this.bookPrice = bookPrice
//        this.bookDescription = bookDescription
//        this.bookColor = bookColor
//        this.bookUrl = bookUrl
//        this.bookSubject = bookSubject
//        this.bookClass = bookClass
//        this.terms = terms
//    }
//
//    constructor()
//}

//data class Terms(
//    //var termDetail:ArrayList<TermDetail> ?= null,
//    var termName:String ?= null,
//    var termDetail:TermDetail ?= null,
//)
//
//data class TermDetail(
//    var termTitle:String ?= null,
//    var termPrice:String ?= null,
//    var termIcon:String ?= null,
//    var topics: Topic ?= null
//)

//data class Topic(
//    var topicDetails: ArrayList<TopicDetails> ?= null,
//)
//
//data class TopicDetails(
//    var topicTitle:String ?= null,
//    var topicIcon:String ?= null,
//    var topicTheme:String ?= null,
//    var lessons:ArrayList<Lesson> ?= null,
//)
//
//data class Lesson(
//    var lessonDetails: ArrayList<LessonDetails>  ?= null,
//)
//
//data class LessonDetails(
//    var lessonTitle:String ?= null,
//    var sections:ArrayList<Sections> ?= null,
//)
//
//data class Sections(
//    var sectionDetails:ArrayList<SectionDetails> ?= null,
//)
//
//data class SectionDetails(
//    var sectionTitle:String ?= null,
//    var content:String ?= null,
//    var media:String ?= null,
//    var mediaCaption:String ?= null,
//    var mediaType:String ?= null,
//)