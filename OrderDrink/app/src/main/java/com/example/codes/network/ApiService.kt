package com.example.restfull_api_1.Networks



import android.hardware.lights.LightsRequest
import com.example.codes.Models.ProductModel
import com.example.codes.Models.SizeModel
import com.example.codes.network.dto.request.AddCartRequest
import com.example.codes.network.dto.request.LoginRequest
import com.example.codes.network.dto.request.ProductName
import com.example.codes.network.dto.request.SignupRequest
import com.example.codes.network.dto.respone.MainRespone
import com.example.codes.network.dto.respone.result.AddCartResult
import com.example.codes.network.dto.respone.result.BannerResult
import com.example.codes.network.dto.respone.result.CategoryResult
import com.example.codes.network.dto.respone.result.ProductResult
import com.example.codes.network.dto.respone.result.SignupResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/create-user")
    fun signUp(@Body request: SignupRequest): Call<MainRespone<SignupResult>>

    @POST("auth/login")
    fun login(@Body request: LoginRequest) : Call<MainRespone<SignupResult>>

    @GET("home/banner")
    fun getBanner(): Call<MainRespone<List<BannerResult>>>

    @GET("home/category")
    fun getCategory(): Call<MainRespone<List<CategoryResult>>>

    @GET("home/product")
    fun getProduct(): Call<MainRespone<List<ProductModel>>>

    @POST("cart/getSize")
    fun getSize(@Body productName: ProductName) : Call<MainRespone<List<SizeModel>>>


    @POST("cart/addToCart")
    fun addToCart(@Body productName: AddCartRequest) : Call<MainRespone<AddCartResult>>







}
