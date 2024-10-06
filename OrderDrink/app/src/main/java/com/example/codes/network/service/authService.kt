package com.example.codes.network.service

import android.content.Context
import android.widget.Toast
import com.example.codes.Models.Users
import com.example.codes.network.RetrofitClient
import com.example.codes.network.dto.respone.SignupRespone


import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object authService {

    private fun signUpService(context: Context, user: Users, callback: (SignupRespone?) -> Unit){
        val call = RetrofitClient.instance.signUp(user)
        call.enqueue(object : Callback<SignupRespone> {
            override fun onResponse(call: Call<SignupRespone>, response: Response<SignupRespone>) {
                if (response.isSuccessful) {
                    // Get the SignupRespone object
                    val signupResponse = response.body()
                    callback(signupResponse)
                } else {
                    // Handle error
                    val errorMessage = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorMessage)
                    val message = jsonObject.getString("message")
                    Toast.makeText(context, "$message", Toast.LENGTH_SHORT).show()
                    callback(null)
                }
            }

            override fun onFailure(call: Call<SignupRespone>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                callback(null)
            }
        })
    }







}