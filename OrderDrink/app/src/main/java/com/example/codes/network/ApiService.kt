package com.example.restfull_api_1.Networks



import com.example.codes.Models.Users
import com.example.codes.network.dto.respone.SignupRespone
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("/signup")
    fun signUp(@Body user: Users): Call<SignupRespone>



}
