package com.example.quizzapp.UI

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.quizzapp.R
import com.example.quizzapp.ViewModel.QuizListViewModel
import com.example.quizzapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var viewModel: QuizListViewModel
    private var position = -1
    private var totalQuestions: Int = 0
    private lateinit var quizId: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this).get(QuizListViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        position = DetailsFragmentArgs.fromBundle(requireArguments()).quizPosition
        viewModel.QuizList.observe(viewLifecycleOwner) { listOfQuiz ->
            val currentQuiz = listOfQuiz[position]
            binding.apply {
                Glide.with(requireContext())
                    .load(currentQuiz.image)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.detailImage)

                detailTitle.text = currentQuiz.name
                detailDescription.text = currentQuiz.desc
                detailDifficultyText.text = currentQuiz.level
                detailQuestionsText.text = currentQuiz.questions.toString()

                totalQuestions = currentQuiz.questions?.toInt() ?: 0
                quizId = currentQuiz.quizId.toString()
            }
        }
        binding.detailBtn.setOnClickListener {
            findNavController().navigate(
                DetailsFragmentDirections.actionDetailsFragmentToQuizFragment(
                    quizId,
                    totalQuestions
                )
            )
        }
    }

}