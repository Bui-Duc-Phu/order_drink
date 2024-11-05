package com.example.codes.Activitys


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.codes.databinding.ActivitySignUpBinding
import com.example.codes.network.dto.request.SignupRequest
import com.example.codes.network.service.authService

class SignUp : AppCompatActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init_()
    }

    private fun init_() {
        binding.apply {
            backBtn.setOnClickListener {
                onBackPressed()
            }
            dangNhapTv.setOnClickListener {
                startActivity(Intent(this@SignUp, Login::class.java))
            }
        }
        checked()
    }
    private fun checked() {
        binding.dangKyBtn.setOnClickListener {
            progressDialog = ProgressDialog.show(this@SignUp, "App", "Loading...", true)
            val userName = binding.nameEdt.text.toString().trim()
            val Email = binding.emailEdt.text.toString().trim()
            val phone = binding.phoneEdt.text.toString().trim()
            val password = binding.passwordEdt.text.toString().trim()
            when {
                userName.isEmpty() -> {
                    binding.nameEdt.error = "Name cannot be empty"
                    progressDialog!!.dismiss()
                    return@setOnClickListener // Return if validation fails
                }
                Email.isEmpty() -> {
                    binding.emailEdt.error = "Email cannot be empty"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
                !Patterns.EMAIL_ADDRESS.matcher(Email).matches() -> { // Using Patterns for email validation
                    binding.emailEdt.error = "Invalid email format"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
                phone.isEmpty() -> {
                    binding.phoneEdt.error = "Phone number cannot be empty"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
                phone.length != 10 -> { // Simple phone number validation
                    binding.phoneEdt.error = "Invalid phone number"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.passwordEdt.error = "Password cannot be empty"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
                password.length <= 6 -> {
                    binding.passwordEdt.error = "Password must be more than 6 characters"
                    progressDialog!!.dismiss()
                    return@setOnClickListener
                }
            }
            progressDialog!!.dismiss()
            val signupRequest = SignupRequest(userName, Email, phone, password)
            authService.signUpService(this,signupRequest,{ signupResponse ->
                if (signupResponse != null) {
                    startActivity(Intent(this,Main::class.java))
                    Toast.makeText(this, "create account successful!", Toast.LENGTH_SHORT).show()
                }
            },{err->Toast.makeText(this, "err: "+ err, Toast.LENGTH_SHORT).show() })
        }
    }



}



















