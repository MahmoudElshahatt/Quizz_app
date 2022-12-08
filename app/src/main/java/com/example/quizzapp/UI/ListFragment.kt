package com.example.quizzapp.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzapp.Adapter.QuizListAdapter
import com.example.quizzapp.Model.Quiz
import com.example.quizzapp.R
import com.example.quizzapp.ViewModel.QuizListViewModel
import com.example.quizzapp.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: QuizListViewModel
    private lateinit var quizListAdapter: QuizListAdapter
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(QuizListViewModel::class.java)
        fadeInAnim = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeOutAnim = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        setUpRecyclerView()
        return binding.root
    }


    fun setUpRecyclerView() {
        quizListAdapter = QuizListAdapter(object : QuizListAdapter.ClickListener {
            override fun onItemClick(quizPosition: Int) {
                findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToDetailsFragment(
                        quizPosition
                    )
                )
            }
        }
        )
        binding.quizList.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = quizListAdapter

            viewModel.QuizList.observe(viewLifecycleOwner) { list ->
                updateUI()
                quizListAdapter.differ.submitList(list)
                quizListAdapter.notifyDataSetChanged()
            }

        }

    }

    private fun updateUI() {
        binding.quizList.startAnimation(fadeInAnim)
        binding.listProgress.startAnimation(fadeOutAnim)
        binding.listProgress.visibility = View.GONE

    }

}