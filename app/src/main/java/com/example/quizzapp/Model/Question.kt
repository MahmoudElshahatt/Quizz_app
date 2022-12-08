package com.example.quizzapp.Model

import com.google.firebase.firestore.DocumentId

data class Question(
    @DocumentId
    var questionId: String? = null,
    var question: String? = null,
    var option_1: String? = null,
    var option_2: String? = null,
    var option_3: String? = null,
    var answer: String? = null,
    var timer: Long? = null
) {
}