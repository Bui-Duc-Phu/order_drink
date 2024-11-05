package com.example.codes.network.service

import android.content.Context
import android.widget.Toast
import com.example.codes.network.RetrofitClient
import com.example.codes.network.dto.request.LoginRequest
import com.example.codes.network.dto.request.SignupRequest
import com.example.codes.network.dto.respone.MainRespone
import com.example.codes.network.dto.respone.result.SignupResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object authService {

    public fun signUpService(context: Context,
                             request: SignupRequest,
                             onSuccess: (MainRespone<SignupResult>?) -> Unit,
                             err: (String) -> Unit
    ) {
        val call = RetrofitClient.instance.signUp(request)
        call.enqueue(object : Callback<MainRespone<SignupResult>> {
            override fun onResponse(call: Call<MainRespone<SignupResult>>, response: Response<MainRespone<SignupResult>>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    println("Response: ${signupResponse!!.result}")
                    onSuccess(signupResponse)
                } else {
                    val errorMessage = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorMessage)
                    val message = jsonObject.getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    err(message)
                }
            }
            override fun onFailure(call: Call<MainRespone<SignupResult>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                onSuccess(null)
            }
        })
    }


    fun loginService(
        context: Context,
        request: LoginRequest,
        onSuccess:(MainRespone<SignupResult>?)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.instance.login(request)
        call.enqueue(object : Callback<MainRespone<SignupResult>> {
            override fun onResponse(
                call: Call<MainRespone<SignupResult>>,
                response: Response<MainRespone<SignupResult>>
            ){
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    println("Response login: ${signupResponse!!.result}")
                    onSuccess(signupResponse)
                } else {
                    val errorMessage = response.errorBody()?.string()
                    val jsonObject = errorMessage?.let {
                        JSONObject(it)
                    }
                    val message = jsonObject!!.getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    err(message)
                }
            }
            override fun onFailure(call: Call<MainRespone<SignupResult>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                onSuccess(null)
            }
        })
    }









}