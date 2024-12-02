package com.example.codes.camera

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class GetImagesActivity : AppCompatActivity() {

    private fun getAllImages(): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        // Truy vấn ContentResolver để lấy tất cả hình ảnh
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id.toString())
                imageUris.add(contentUri)
            }
        }
        return imageUris
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lấy tất cả ảnh
        val imageUris = getAllImages()

        // Cấp quyền cho App B để đọc URI
        imageUris.forEach { uri ->
            grantUriPermission(
                "com.example.attack_camera", // Package của App B
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }

        // Trả kết quả về App B
        val resultIntent = Intent().apply {
            putParcelableArrayListExtra("imageUris", ArrayList(imageUris))
        }
        setResult(RESULT_OK, resultIntent)
        finish() // Kết thúc Activity
    }

}