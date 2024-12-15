package com.example.codes.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import androidx.annotation.RequiresApi

class SmsForegroundService : Service() {

    private lateinit var smsReceiver: BroadcastReceiver

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        // Khởi tạo smsReceiver
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
                }
            }
        }
        registerReceiver(smsReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Sử dụng START_STICKY để duy trì dịch vụ
    }

    override fun onDestroy() {
        super.onDestroy()
        // Hủy đăng ký BroadcastReceiver khi Service bị dừng
        unregisterReceiver(smsReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
