package com.example.quizzapp.Repository

import com.example.quizzapp.Model.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FireBaseRepository(private val callBack: FirebaseCallBack) {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var collection = firestore.collection("QuizList")


    fun getQuizList() = CoroutineScope(Dispatchers.IO).launch {
        collection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                callBack.onResponse(task.result.toObjects(Quiz::class.java))
            } else {
                task.exception?.let { callBack.onError(it) }
            }
        }
    }

    interface FirebaseCallBack {
        fun onResponse(quizList: List<Quiz>)
        fun onError(e: Exception)

    }
}