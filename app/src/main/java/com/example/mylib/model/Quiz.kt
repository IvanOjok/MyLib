package com.example.mylib.model

class Quiz {
    var answers: List<String> ?= null
    var choice: String? = null
    var choiceType: String? = null
    var media: String? = null
    var mediaType: String? = null
    var question: String? = null
    var questionOptions: List<String> ?= null

    constructor()

    constructor(
        answers: List<String>?,
        choice: String?,
        choiceType: String?,
        media: String?,
        mediaType: String?,
        question: String?,
        questionOptions: List<String>?
    ) {
        this.answers = answers
        this.choice = choice
        this.choiceType = choiceType
        this.media = media
        this.mediaType = mediaType
        this.question = question
        this.questionOptions = questionOptions
    }

}


class Submission {
    var quiz = Quiz()
    var correctness: MutableList<Int> ?= null
    var submission: MutableList<String> ?= null

    constructor()

    constructor(
        quiz: Quiz,
        correctness: MutableList<Int>,
        submission: MutableList<String>
    ) {
        this.quiz = quiz
        this.correctness = correctness
        this.submission = submission
    }

}