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


    // Kiểm tra thông tin đăng nhập
    private fun checked() {
        var email = binding.emailEdt.text.toString().trim()
        var password = binding.passwordEdt.text.toString().trim()
        when {
            TextUtils.isEmpty(email) -> Toast.makeText(this,
                getString(R.string.email_not_null), Toast.LENGTH_SHORT)
                .show()

            TextUtils.isEmpty(password) -> Toast.makeText(
                this,
                getString(R.string.password_not_null),
                Toast.LENGTH_SHORT
            ).show()

            else -> {
                // Kiểm tra email có đúng định dạng không, nếu không thì đăng nhập bằng tên đăng nhập
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) loginWithUsername(
                    email,
                    password
                )
                else loginWithEmail(email, password)
            }
        }
    }


    private fun loginWithUsername(username: String, password: String) {
        val ref = FirebaseDatabase
            .getInstance("https://coffe-app-19ec3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isFinishing) {
                    progressDialog = ProgressDialog.show(this@Login, "App", "Loading...", true)
                }

                for (snapshot in snapshot.children) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user!!.userName.equals(username)) {
                        println(getString(R.string.checked_usser_successfull))
                        loginWithEmail(user.email, password)
                        progressDialog!!.dismiss()
                        return
                    }
                }
                progressDialog!!.dismiss()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.t_n_ng_nh_p_kh_ng_t_n_t_i),
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.login_with_username_connect_database_faile),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    @SuppressLint("NotConstructor")
    private fun loginWithEmail(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    auth.signOut()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                progressDialog = ProgressDialog.show(this, "App", "Login...", true)
                                binding.emailEdt.setText("")
                                binding.passwordEdt.setText("")
                                progressDialog!!.dismiss()
                                FirebaseFunction.WriteDeviceId(
                                    applicationContext,
                                    auth.currentUser!!.uid
                                )
                                checkTypeAccount(email)
                            }
                        }
                }
            }.addOnFailureListener { e ->

                Toast.makeText(this, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun checkTypeAccount(email: String) {
        val ref = FirebaseDatabase
            .getInstance("https://coffe-app-19ec3-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")
            .orderByChild("email")
            .equalTo(email)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(Users::class.java)
                    if (user != null) {
                        if (user.typeAccount == "2") {
                            DataHandler.setTypeAccount("2")
                            startActivity(Intent(this@Login, MainAdmin::class.java))
                            finish()
                        } else {
                            DataHandler.setTypeAccount("1")
                            val intent = Intent(this@Login, Main::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors.
            }
        })
    }


}