package com.example.codes.network

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "users.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
    }
    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USERNAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")")
        db.execSQL(createTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    @SuppressLint("SuspiciousIndentation")
    fun login(username: String, password: String): Boolean {

        try {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE $COLUMN_USERNAME = '$username' AND $COLUMN_PASSWORD = '$password'"
        val cursor: Cursor = db.rawQuery(query, null)
            println("sql data : " + cursor)
        val loginSuccessful = cursor.count > 0
        cursor.close()
        db.close()
        return loginSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
           return false
        }
    }



//    fun login(username: String, password: String): Boolean {
//        val db = this.readableDatabase
//        val columns = arrayOf("*") // Lấy tất cả các cột
//        val selection = "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
//        val selectionArgs = arrayOf(username, password)
//
//        val cursor = db.query(
//            TABLE_USERS,   // Tên bảng
//            columns,       // Các cột cần truy vấn
//            selection,     // Điều kiện WHERE
//            selectionArgs, // Các giá trị của điều kiện
//            null,          // Nhóm (groupBy)
//            null,          // Điều kiện HAVING
//            null           // Sắp xếp (orderBy)
//        )
//
//        val loginSuccessful = cursor.count > 0
//        cursor.close()
//        db.close()
//
//        return loginSuccessful
//    }





}
