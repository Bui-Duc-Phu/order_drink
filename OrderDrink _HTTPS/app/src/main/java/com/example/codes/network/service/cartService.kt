package com.example.codes.network.service

import android.content.Context
import android.widget.Toast
import com.example.codes.Models.SizeModel
import com.example.codes.network.RetrofitClient
import com.example.codes.network.dto.request.AddCartRequest
import com.example.codes.network.dto.request.ProductName
import com.example.codes.network.dto.respone.MainRespone
import com.example.codes.network.dto.respone.result.AddCartResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object cartService {



    fun getSize(
        context: Context,
        productName :ProductName,
        onSuccess:(List<SizeModel>)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.getInstance(context).getSize(productName)
        call.enqueue(object : Callback<MainRespone<List<SizeModel>>> {
            override fun onResponse(
                call: Call<MainRespone<List<SizeModel>>>,
                response: Response<MainRespone<List<SizeModel>>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    println("Response getsize: ${res!!.result}")
                    res.result?.let { onSuccess(it) }
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
            override fun onFailure(call: Call<MainRespone<List<SizeModel>>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                err(t.message.toString())

            }

        })
    }



    fun addCart(
        context: Context,
        cartProduct :AddCartRequest,
        onSuccess:(AddCartResult)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.getInstance(context).addToCart(cartProduct)
        call.enqueue(object : Callback<MainRespone<AddCartResult>> {
            override fun onResponse(
                call: Call<MainRespone<AddCartResult>>,
                response: Response<MainRespone<AddCartResult>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    println("Response getsize: ${res!!.result}")


                    res.result?.let { onSuccess(it) }
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

            override fun onFailure(call: Call<MainRespone<AddCartResult>>, t: Throwable) {

            }

        })
    }

}