package com.example.codes.network.service

import android.content.Context
import android.widget.Toast
import com.example.codes.Models.CategoryModel
import com.example.codes.Models.ProductModel
import com.example.codes.network.RetrofitClient
import com.example.codes.network.dto.respone.MainRespone
import com.example.codes.network.dto.respone.result.BannerResult
import com.example.codes.network.dto.respone.result.CategoryResult
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object homeService {

    fun getBanner(
        context: Context,
        onSuccess:(List<BannerResult>?)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.getInstance(context).getBanner()
        call.enqueue(object : Callback<MainRespone<List<BannerResult>>> {
            override fun onResponse(
                call: Call<MainRespone<List<BannerResult>>>,
                response: Response<MainRespone<List<BannerResult>>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    println("Response getbaner: ${res!!.result}")
                    onSuccess(res.result)
                } else {
                    val errorMessage = response.errorBody()?.string()
                    val jsonObject = errorMessage?.let {
                        JSONObject(it)
                    }
                    val message = jsonObject!!.getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    println("get List banner err" + message)
                    err(message)
                }
            }
            override fun onFailure(call: Call<MainRespone<List<BannerResult>>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println("err get banner " + t.message)
                onSuccess(null)
            }
        })
    }

    fun getCategory(
        context: Context,
        onSuccess:(MutableList<CategoryModel?>)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.getInstance(context).getCategory()
        call.enqueue(object : Callback<MainRespone<List<CategoryResult>>> {
            override fun onResponse(
                call: Call<MainRespone<List<CategoryResult>>>,
                response: Response<MainRespone<List<CategoryResult>>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    println("Response getbaner: ${res!!.result}")
                    val categoryModelsList: MutableList<CategoryModel?> = res.result?.map { categoryResult ->
                        CategoryModel(categoryResult.name)
                    }!!.toMutableList()


                    println("Response getbaner 2: ${ categoryModelsList }")
                    onSuccess(categoryModelsList)
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
            override fun onFailure(call: Call<MainRespone<List<CategoryResult>>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                err(t.message.toString())

            }

        })
    }

    fun GetProduct(
        context: Context,
        onSuccess:(List<ProductModel>)->Unit,
        err: (String) -> Unit
    ){
        val call = RetrofitClient.getInstance(context).getProduct()
        call.enqueue(object : Callback<MainRespone<List<ProductModel>>> {
            override fun onResponse(
                call: Call<MainRespone<List<ProductModel>>>,
                response: Response<MainRespone<List<ProductModel>>>
            ) {
                if (response.isSuccessful) {
                    val res = response.body()
                    println("Response getProduct: ${res!!.result}")
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
            override fun onFailure(call: Call<MainRespone<List<ProductModel>>>, t: Throwable) {
                Toast.makeText(context, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()
                println(t.message)
                err(t.message.toString())

            }

        })
    }

}