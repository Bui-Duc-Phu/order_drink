package com.example.codes.Activitys

import android.app.ProgressDialog
import android.content.Intent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.codes.Administrator.MyApp

import com.example.codes.Ultils.DatabaseHelper
import com.example.codes.Ultils.MySharedPreferences
import com.example.codes.Ultils.fix.MySharedPreferencesFix
import com.example.codes.databinding.ActivityLoginBinding
import com.example.codes.network.dto.request.LoginRequest
import com.example.codes.network.service.authService


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Login : AppCompatActivity() {


    lateinit var auth: FirebaseAuth
    lateinit var firebaseUser: FirebaseUser
    private var progressDialog: ProgressDialog? = null
    lateinit var  mySharedPreferences : MySharedPreferences
    private var isCheckAccount : Boolean = false
    private lateinit var databaseHelper: DatabaseHelper

    lateinit var  mySharedPreferencesFix : MySharedPreferencesFix

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        mySharedPreferencesFix = MySharedPreferencesFix(this)
//        mySharedPreferencesFix.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFiY0BnbWFpbC5jb20iLCJpZCI6IjVmYzEzNGNmLWJkZWEtNDBhMS04NzUzLTY1M2U4MDQ0ZDYwNCIsImlhdCI6MTczMzU0NzY5NiwiZXhwIjoxNzMzNjM0MDk2fQ.F6hQR2kprDitTIH5GqHLB-XCzTCohZMmd_MegWp_6fM")
//        mySharedPreferencesFix.setUserId("5fc134cf-bdea-40a1-8753-653e8044d604")
//


        mySharedPreferences = MySharedPreferences(this)
//        mySharedPreferences.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFiY0BnbWFpbC5jb20iLCJpZCI6IjVmYzEzNGNmLWJkZWEtNDBhMS04NzUzLTY1M2U4MDQ0ZDYwNCIsImlhdCI6MTczMzU0NzY5NiwiZXhwIjoxNzMzNjM0MDk2fQ.F6hQR2kprDitTIH5GqHLB-XCzTCohZMmd_MegWp_6fM")
//        mySharedPreferences.setUserId("5fc134cf-bdea-40a1-8753-653e8044d604")
//        mySharedPreferences.setApiKey("sdsdsd2hasdh232fysdassdsddasdgasdujvsbdjsd")
//



        databaseHelper = DatabaseHelper(this)




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

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    isCheckAccount = true

                } else {
                    isCheckAccount = false

                }
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

        if(isCheckAccount){
            println("vao roif")
            if (databaseHelper.login(email, password)) {
                println("vao roifs")
                email = "phuhk9@gmail.com"
                password="phuhk123"
            }else{
                println("login false")
            }
        }

        authService.loginService(this,
            LoginRequest(email, password),
            { onSuccess ->
                if (onSuccess != null) {
                    Toast.makeText(this, "Create account successful!", Toast.LENGTH_SHORT).show()

                    // Check if the access token is not null
                    val accessToken = onSuccess.result?.accesstoken
                    if (!accessToken.isNullOrEmpty()) {
                        mySharedPreferences.setToken(accessToken)
                        mySharedPreferences.setUserId(onSuccess.result.id)
                        mySharedPreferences.setApiKey("sdsdsd2hasdhfysdasdasdgasdujvsbdjsd")

                        startActivity(Intent(this, Main::class.java))
                    } else {
                        // Handle the case where the access token is null or empty
                        Toast.makeText(this, "Failed to get access token", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            { err ->
                Toast.makeText(this, "Error: $err", Toast.LENGTH_SHORT).show()
                println("Error: $err")
            })

    }









}