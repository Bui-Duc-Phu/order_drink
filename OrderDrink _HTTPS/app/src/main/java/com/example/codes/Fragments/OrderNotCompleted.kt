package com.example.codes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.codes.databinding.FragmentOrderNotCompletedBinding


import com.google.firebase.auth.FirebaseAuth


class OrderNotCompleted : Fragment() {
    lateinit var binding: FragmentOrderNotCompletedBinding
    lateinit var auth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderNotCompletedBinding.inflate(layoutInflater,container,false)
        auth = FirebaseAuth.getInstance()

        init_()
        return binding.root
    }

    private fun init_() {
        recylerview()

    }

    private fun recylerview() {
    }




}