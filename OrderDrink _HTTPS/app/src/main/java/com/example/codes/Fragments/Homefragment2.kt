package com.example.codes.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.codes.R
import com.example.codes.databinding.FragmentHomeBinding
import com.example.codes.databinding.FragmentHomefragment2Binding


class Homefragment2 : Fragment() {


    private lateinit var binding: FragmentHomefragment2Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _init()

        binding = FragmentHomefragment2Binding.inflate(inflater, container, false)
        return binding.root
    }

    private fun _init() {

    }

}