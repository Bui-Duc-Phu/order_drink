package com.example.codes.camera

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log

class ImageProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.codes.provider"
        const val IMAGES = 1
        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "images", IMAGES)
        }
    }

    private val images = listOf(
        "image1.jpg",
        "image2.jpg"
    ) // Giả lập danh sách hình ảnh được lưu trữ

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            IMAGES -> {
                val cursor = MatrixCursor(arrayOf("file_name"))
                images.forEach { cursor.addRow(arrayOf(it)) }
                cursor
            }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}
