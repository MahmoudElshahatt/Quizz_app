package com.example.quizzapp.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzapp.Model.Question
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class QuizViewModel : ViewModel() {

    private var _fireStore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val fireStore
        get() = _fireStore

    private var _firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
      val firebaseAuth
      get() = _firebaseAuth

    private var _errorState: MutableLiveData<Boolean> = MutableLiveData()
    val ErrorState: LiveData<Boolean>
        get() = _errorState

    private var _Questions: MutableLiveData<List<Question>?> = MutableLiveData()
    val Questions: LiveData<List<Question>?>
        get() = _Questions

    init {
        _errorState.value = false
        _Questions.value = null
    }

    fun getQuestions(quizId: String) {
        _fireStore.collection("QuizList").document(quizId).collection("Questions")
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _Questions.value = task.result.toObjects(Question::class.java)
                } else {
                    _errorState.value = true
                }
            }
    }
}