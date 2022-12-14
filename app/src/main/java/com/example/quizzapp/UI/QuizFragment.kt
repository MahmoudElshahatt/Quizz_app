package com.example.quizzapp.UI

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.quizzapp.Model.Question
import com.example.quizzapp.R
import com.example.quizzapp.ViewModel.QuizViewModel
import com.example.quizzapp.databinding.FragmentQuizBinding

class QuizFragment : Fragment() {

    private var correctAnswers = 0
    private var wrongAnswers = 0
    private var notAnswered = 0
    private var quizName: String? = null
    private lateinit var currentUserId: String
    private lateinit var binding: FragmentQuizBinding
    private lateinit var viewModel: QuizViewModel
    private lateinit var quizId: String
    private lateinit var questions: List<Question>
    private lateinit var timer: CountDownTimer
    private var totalQuestions: Int = 0
    private var canAnswer: Boolean = true
    private var currentQuestion: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        intialization()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getQuestions(quizId)
        if (viewModel.firebaseAuth.currentUser != null) {
            currentUserId = viewModel.firebaseAuth.currentUser!!.uid
        } else {
            findNavController().popBackStack()
        }
        setListeners()
        setObservations()

    }

    private fun intialization() {
        binding = FragmentQuizBinding.inflate(layoutInflater)

        quizId = QuizFragmentArgs.fromBundle(requireArguments()).quizId
        totalQuestions = QuizFragmentArgs.fromBundle(requireArguments()).totalQuestions
        quizName = QuizFragmentArgs.fromBundle(requireArguments()).quizName
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    private fun setObservations() {
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

    private fun setListeners() {
        binding.quizCloseBtn.setOnClickListener {
            val dialogBuilder =
                AlertDialog.Builder(requireContext(), AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                    .setMessage("are you sure you want to exit ?")
                    .setCancelable(false)
                    .setPositiveButton("I am sure") { _, _ ->
                        findNavController().popBackStack()
                    }
                    // negative button text and action
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                    }
            val alert = dialogBuilder.create()
            alert.show()

        }

        binding.quizOptionOne.setOnClickListener { verifyAnswer(binding.quizOptionOne) }

        binding.quizOptionTwo.setOnClickListener { verifyAnswer(binding.quizOptionTwo) }

        binding.quizOptionThree.setOnClickListener { verifyAnswer(binding.quizOptionThree) }

        binding.quizNextBtn.setOnClickListener {
            if (currentQuestion == questions.size - 1) {
                submitResult()
            } else {
                currentQuestion++
                loadQuestion(currentQuestion)
                resetOptions()
            }
        }
    }

    private fun loadUI() {
        binding.quizTitle.text = quizName + ""
        enableOptions()

        loadQuestion(currentQuestion)
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
            quizNumberText.text = "" + (quesNumber + 1)
            //Question Options
            quizOptionOne.text = questions[quesNumber].option_1
            quizOptionTwo.text = questions[quesNumber].option_2
            quizOptionThree.text = questions[quesNumber].option_3

            canAnswer = true
            //Question Timer
            startTimer(quesNumber)
        }
    }

    private fun startTimer(quesNumber: Int) {
        val timeToAnswer: Long = questions[quesNumber].timer!!
        binding.quizQuestionPrograss.visibility = View.VISIBLE
        binding.questionTime.text = timeToAnswer.toString()

        timer = object : CountDownTimer(timeToAnswer!! * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.questionTime.text = "" + (millisUntilFinished / 1000)
                binding.quizQuestionPrograss.apply {
                    progress = ((millisUntilFinished / (timeToAnswer * 10)).toFloat())
                }
            }

            override fun onFinish() {
                canAnswer = false
                binding.quizQuestionPrograss.visibility = View.GONE

                showNotAnsweredState()
            }
        }
        timer.start()
    }

    private fun showNotAnsweredState() {
        binding.answerFeedback.text = "Time Up! No Answer was submitted"
        notAnswered++;
        showNextButton()
    }

    private fun verifyAnswer(selectedAnswerBtn: Button) {
        if (canAnswer) {
            if (questions[currentQuestion].answer == selectedAnswerBtn.text) {
                setCorrectState(selectedAnswerBtn)
            } else {
                setWrongState(selectedAnswerBtn)
            }
            canAnswer = false
            timer.cancel()

            showNextButton()
        }
    }

    private fun setWrongState(selectedAnswerBtn: Button) {
        wrongAnswers++
        selectedAnswerBtn.setBackgroundColor(resources.getColor(R.color.colorAccent))
        binding.answerFeedback.text =
            "Wrong Answer \n Correct Answer is " + questions[currentQuestion].answer
        binding.answerFeedback.setTextColor((resources.getColor(R.color.colorAccent)))
    }

    private fun setCorrectState(selectedAnswerBtn: Button) {
        correctAnswers++
        selectedAnswerBtn.setBackgroundColor(resources.getColor(R.color.colorPrimary))
        binding.answerFeedback.text = "Correct Answer !!"
        binding.answerFeedback.setTextColor((resources.getColor(R.color.colorPrimary)))
    }

    private fun showNextButton() {
        if (currentQuestion == questions.size - 1) {
            binding.answerFeedback.text = "Showing results"
        }
        binding.apply {
            quizNextBtn.visibility = View.VISIBLE
            answerFeedback.visibility = View.VISIBLE
            quizNextBtn.isEnabled = true
        }
    }

    private fun resetOptions() {
        binding.apply {
            quizOptionOne.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.outline_light_button_bg,
                    null
                )
            )
            quizOptionTwo.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.outline_light_button_bg,
                    null
                )
            )
            quizOptionThree.setBackgroundDrawable(
                resources.getDrawable(
                    R.drawable.outline_light_button_bg,
                    null
                )
            )

            quizNextBtn.visibility = View.INVISIBLE
            answerFeedback.visibility = View.INVISIBLE
            quizNextBtn.isEnabled = false

        }
    }

    private fun submitResult() {
        //Make a hash map
        val result = HashMap<String, String>()
        result.put("correct", correctAnswers.toString())
        result.put("wrong", wrongAnswers.toString())
        result.put("unanswered", notAnswered.toString())


        viewModel.fireStore.collection("QuizList").document(quizId)
            .collection("Results").document(currentUserId).set(result)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(
                        QuizFragmentDirections.actionQuizFragmentToResultFragment(
                            quizId
                        )
                    )
                } else {
                    binding.quizTitle.text = task.exception?.message
                }
            }
    }


}