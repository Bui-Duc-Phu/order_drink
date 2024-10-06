package com.example.codes.Activitys

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.TextUtils
import android.util.Patterns
import android.view.WindowManager
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast

import com.example.codes.Firebase.FirebaseFunction
import com.example.codes.Firebase.FirebaseUpdate
import com.example.codes.Firebase.OTP_Athen_Phone

import com.example.codes.Models.Users
import com.example.codes.R
import com.example.codes.databinding.ActivitySignUpBinding
import com.example.codes.databinding.DialogChoseTypeAuthenBinding
import com.example.codes.databinding.DialogCustomBinding
import com.example.codes.databinding.DialogCustomOtpBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.random.Random

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




        }


    }


    private fun createAcount(userName: String, email: String, password: String, phone: String) {


    }


    private fun checkUserName(userName_: String, email: String, callback: (Int) -> Unit) {
    }


}



















