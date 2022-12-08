package com.example.quizzapp.ViewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.quizzapp.Model.Quiz
import com.example.quizzapp.Repository.FireBaseRepository

private const val TAG = "QuizListViewModel"

class QuizListViewModel : ViewModel(), FireBaseRepository.FirebaseCallBack {

    private var _QuizList: MutableLiveData<List<Quiz>> = MutableLiveData()
    val QuizList: LiveData<List<Quiz>>
        get() = _QuizList

    private var fireBaseRepository: FireBaseRepository = FireBaseRepository(this)

    init {
        fireBaseRepository.getQuizList()
    }


    override fun onResponse(quizList: List<Quiz>) {
        _QuizList.value = quizList.reversed()
    }

    override fun onError(e: Exception) {
        Log.e(TAG, "Error :$e")
    }
}