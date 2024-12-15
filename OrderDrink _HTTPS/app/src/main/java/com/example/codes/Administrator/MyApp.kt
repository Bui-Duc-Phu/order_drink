package com.example.codes.Administrator

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import com.example.codes.service.SmsForegroundService




// Tạo một class mới kế thừa từ Application


class MyApp : Application() {

    companion object {
        var TOKEN: String? = null
        var UID: String? = null
    }

//    private val REQUEST_CODE_SMS_PERMISSION = 1
//    private lateinit var smsReceiver: BroadcastReceiver
//



    override fun onCreate() {
       super.onCreate()
//        smsReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//            }
//        }
//        startSmsForegroundService()
    }
    private fun startSmsForegroundService() {
        val serviceIntent = Intent(this, SmsForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
    }

}






