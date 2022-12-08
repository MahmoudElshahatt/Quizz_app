package com.example.quizzapp.UI

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.quizzapp.R
import com.example.quizzapp.databinding.FragmentStartBinding
import com.google.firebase.auth.FirebaseAuth

class StartFragment : Fragment() {
    private lateinit var binding: FragmentStartBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        navController = this.findNavController()
        binding.startState.text = "Checking User Account!!"

    }

    override fun onStart() {
        super.onStart()
        checkUser()
    }

    private fun checkUser() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {

            binding.startState.text = "Creating Account..."
            firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate(R.id.action_startFragment_to_listFragment)
                } else {
                    Log.d("Start Fragment", "" + task.exception)
                }
            }
        } else {
            navController.navigate(R.id.action_startFragment_to_listFragment)
        }
    }

}