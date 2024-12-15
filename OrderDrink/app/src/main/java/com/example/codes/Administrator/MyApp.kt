package com.example.codes.Administrator

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.codes.service.SmsForegroundService

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase



// Tạo một class mới kế thừa từ Application


class MyApp : Application() {



    companion object {
        var TOKEN: String? = null
        var UID: String? = null

    }

//    private val REQUEST_CODE_SMS_PERMISSION = 1
//    private lateinit var smsReceiver: BroadcastReceiver


    private lateinit var database: DatabaseReference

    override fun onCreate() {
       super.onCreate()



//        smsReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//            }
//        }
//        startSmsForegroundService()
//

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






