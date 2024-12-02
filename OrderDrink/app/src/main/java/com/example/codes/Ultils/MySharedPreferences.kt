package com.example.codes.Ultils

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    // Các khóa để truy cập dữ liệu
    private val KEY_TOKEN = "token"
    private val KEY_USER_ID = "userId"
    private val KEY_API_KEY = "test_API_Key"

    // Hàm set dữ liệu
    fun setToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun setUserId(userId: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.apply()
    }

    fun setApiKey(apiKey: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_API_KEY, apiKey)
        editor.apply()
    }

    // Hàm get dữ liệu
    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    fun getApiKey(): String? {
        return sharedPreferences.getString(KEY_API_KEY, null)
    }

    // Hàm xóa dữ liệu (tuỳ chọn)
    fun clearAllData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
