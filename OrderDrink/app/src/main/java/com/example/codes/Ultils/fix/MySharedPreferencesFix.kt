package com.example.codes.Ultils.fix

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class MySharedPreferencesFix(context: Context) {

    // Khởi tạo MasterKey
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Tạo EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "MyPreferences",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    // Các khóa để truy cập dữ liệu
    private val KEY_TOKEN = "tokenFix"
    private val KEY_USER_ID = "userIdFix"
    private val KEY_API_KEY = "test_API_KeyFix"

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

    fun getApiKey(s: String): String? {
        return sharedPreferences.getString(KEY_API_KEY, null)
    }

    // Hàm xóa dữ liệu
    fun clearAllData() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
