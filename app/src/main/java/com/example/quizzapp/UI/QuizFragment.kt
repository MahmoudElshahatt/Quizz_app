package com.example.quizzapp.UI

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.quizzapp.Model.Question
import com.example.quizzapp.ViewModel.QuizViewModel
import com.example.quizzapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var quizId: String
    private lateinit var questions: List<Question>
    private var totalQuestions: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(layoutInflater)

        quizId = QuizFragmentArgs.fromBundle(requireArguments()).quizId
        totalQuestions = QuizFragmentArgs.fromBundle(requireArguments()).totalQuestions
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuestions(quizId)
        viewModel.Questions.observe(viewLifecycleOwner) { list ->
            if (list != null) {
                questions = list
                loadUI()
            }
        }
        viewModel.ErrorState.observe(viewLifecycleOwner) { error ->
            if (error) {
                binding.quizTitle.text = "Error Loading Data"
            }
        }

    }

    private fun loadUI() {
        binding.apply {
            quizTitle.text = "Quiz Data Loaded"
        }
        //Enable Options
        enableOptions()

        //Load Questions
        loadQuestion(1)
    }

    private fun enableOptions() {
        binding.apply {
            quizOptionOne.visibility = View.VISIBLE
            quizOptionTwo.visibility = View.VISIBLE
            quizOptionThree.visibility = View.VISIBLE

            quizNextBtn.visibility = View.INVISIBLE
            answerFeedback.visibility = View.INVISIBLE
            quizNextBtn.isEnabled = false

        }
    }

    private fun loadQuestion(quesNumber: Int) {
        binding.apply {
            //Questions Text
            questionText.text = questions[quesNumber].question
            quizNumberText.text = "" + quesNumber
            //Question Options
            quizOptionOne.text = questions[quesNumber].option_1
            quizOptionTwo.text = questions[quesNumber].option_2
            quizOptionThree.text = questions[quesNumber].option_3
            //Question Timer
            startTimer(quesNumber)
        }
    }

    private fun startTimer(quesNumber: Int) {
        val timeToAnswer = questions[quesNumber].timer
        binding.questionTime.text = timeToAnswer.toString()

        val timer = object : CountDownTimer(timeToAnswer!! * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.questionTime.text = "" + (millisUntilFinished / 1000)
            }

            override fun onFinish() {

            }
        }
        timer.start()


    }


}