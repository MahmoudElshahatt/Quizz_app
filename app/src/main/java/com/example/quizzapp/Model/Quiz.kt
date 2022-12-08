package com.example.quizzapp.Model

import com.google.firebase.firestore.DocumentId

data class Quiz(
    @DocumentId
    var quizId: String?=null,
    var name: String?=null,
    var desc: String?=null,
    var image: String?=null,
    var level: String?=null,
    var visibility: String?=null,
    var questions: Int?=null
)

