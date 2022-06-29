package com.example.mylib.model

class Cart {
    var id:String ?= null
    var title:String ?= null
    var term:String ?= null
    var price:String ?= null
    var url:String ?= null
    var titleTerm:String ?=null
    var bookClass:String ?=null

    constructor(id:String, title:String, term:String, price:String, url: String, titleTerm:String, bookClass:String){
        this.id = id
        this.title= title
        this.term = term
        this.price = price
        this.url = url
        this.titleTerm = titleTerm
        this.bookClass = bookClass
    }

    constructor()
}


class Library {
    var id:String ?= null
    var title:String ?= null
    var term:String ?= null
    var price:String ?= null
    var url:String ?= null
    var titleTerm:String ?=null
    var startDate:String ?=null
    var endDate:String ?=null
    var bookClass:String ?=null

    constructor(id:String, title:String, term:String, price:String, url: String, titleTerm:String, startDate:String,endDate:String, bookClass:String){
        this.id = id
        this.title= title
        this.term = term
        this.price = price
        this.url = url
        this.titleTerm = titleTerm
        this.startDate = startDate
        this.endDate = endDate
        this.bookClass = bookClass
    }

    constructor()
}