package com.example.quizzapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzapp.Model.Question
import com.google.firebase.firestore.FirebaseFirestore

class QuizViewModel : ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var _ErrorState: MutableLiveData<Boolean> = MutableLiveData()
    val ErrorState: LiveData<Boolean>
        get() = _ErrorState

    private var _Questions: MutableLiveData<List<Question>?> = MutableLiveData()
    val Questions: LiveData<List<Question>?>
        get() = _Questions

    init {
        _ErrorState.value = false
        _Questions.value = null
    }

    fun getQuestions(quizId: String) {
        firestore.collection("QuizList").document(quizId).collection("Questions")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _Questions.value = task.result.toObjects(Question::class.java)
                } else {
                    _ErrorState.value = true
                }
            }
    }
}