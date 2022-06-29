package com.example.mylib.model

class Profile {
    var id:String ?= null
    var name:String ?= null
    var gender:String ?= null
    var stdClass:String ?= null

    constructor(id:String, name: String, gender:String, stdClass:String){
        this.id = id
        this.name = name
        this.gender = gender
        this.stdClass = stdClass
    }

    constructor()
}