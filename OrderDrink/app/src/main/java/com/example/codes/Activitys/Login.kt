package com.example.codes.Activitys

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.codes.Administrator.Activitys.MainAdmin

import com.example.codes.Firebase.DataHandler
import com.example.codes.Firebase.FirebaseFunction

import com.example.codes.Models.Users
import com.example.codes.R
import com.example.codes.databinding.ActivityLoginBinding
import com.example.codes.network.dto.request.LoginRequest
import com.example.codes.network.service.authService


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Login : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    private var progressDialog: ProgressDialog? = null


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        init_()
    }
    private fun init_() {
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
            }
            dangKyTv.setOnClickListener {
                startActivity(Intent(this@Login, SignUp::class.java))
            }
            dangNhapBtn.setOnClickListener { checked() }
            quyenMatKhauTv.setOnClickListener {
                startActivity(Intent(this@Login, InputEmailActivity::class.java))
            }
        }
    }
    private fun checked() {
        var email = binding.emailEdt.text.toString().trim()
        var password = binding.passwordEdt.text.toString().trim()
        when{
            email.isEmpty()->{
                binding.emailEdt.error = "email cannot be empty"
                return
            }
            password.isEmpty()->{
                binding.passwordEdt.error = "password cannot be empty"
                return
            }
        }
        authService.loginService(this,
            LoginRequest(email,password), { onSuccess ->
                if (onSuccess  != null) {
                    Toast.makeText(this, "create account successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Main::class.java))
                }

        },{err-> Toast.makeText(this, "err: "+ err, Toast.LENGTH_SHORT).show() })
    }









}