package com.example.codes

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log

class CameraReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.codes.OPEN_CAMERA") {
            Log.d("CameraReceiver", "Received request to open camera")

            // Tạo Intent để mở camera
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Bắt buộc để khởi động Activity từ BroadcastReceiver
            }
            context?.startActivity(cameraIntent)
        }
    }
}
