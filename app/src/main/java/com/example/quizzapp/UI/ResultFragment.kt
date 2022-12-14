package com.example.quizzapp.UI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzapp.ViewModel.QuizViewModel
import com.example.quizzapp.databinding.FragmentResultBinding
import com.google.firebase.firestore.DocumentSnapshot


class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var quizId: String
    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentResultBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quizId = ResultFragmentArgs.fromBundle(requireArguments()).quizId
        if (viewModel.firebaseAuth.currentUser != null) {
            currentUserId = viewModel.firebaseAuth.currentUser!!.uid
        } else {
            findNavController().popBackStack()
        }
        getUserResult()
    }

    private fun updateUI(result: DocumentSnapshot) {
        binding.apply {
            val correct = result.get("correct").toString()
            val wrong = result.get("wrong").toString()
            val unAnswered = result.get("unanswered").toString()

            correctAnswerText.text = correct
            wrongAnswerText.text = wrong
            notAnsweredText.text = unAnswered

            resultsHomeBtn.setOnClickListener {
                findNavController().navigate(ResultFragmentDirections.actionResultFragmentToListFragment())
            }
            val total = correct.toLong() + wrong.toLong() + unAnswered.toLong()
            val percent = (correct.toLong() * 100) / total

            resultPercent.text = "$percent%"
            resultProgress.progress = percent.toFloat()
        }
    }

    private fun getUserResult() {
        viewModel.fireStore.collection("QuizList").document(quizId)
            .collection("Results").document(currentUserId).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val result: DocumentSnapshot = task.result
                    updateUI(result)
                }

            }
    }
}