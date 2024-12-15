package com.example.codes.Activitys

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codes.databinding.ActivityLoginSqlBinding



import android.Manifest
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor

import android.provider.ContactsContract
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.codes.Ultils.DatabaseHelper


class loginSQL : AppCompatActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, fetch contacts
                getContacts()
            } else {
                // Permission denied, show toast
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    private lateinit var databaseHelper: DatabaseHelper
    private val binding: ActivityLoginSqlBinding by lazy {
        ActivityLoginSqlBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        databaseHelper = DatabaseHelper(this)

        createDefaultUser()

        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Gọi hàm loginUser từ databaseHelper
            if (databaseHelper.login(username, password)) {
                binding.resultTextView.text = "Login successful!"
            } else {
                binding.resultTextView.text = "Login failed. Please try again."
            }
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, fetch contacts
            binding.loginButton.setOnClickListener {
                getContacts()
            }
        } else {
            // Request permission if not granted
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }


    }

    private fun createDefaultUser() {
        val db = databaseHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_USERNAME, "admin")
            put(DatabaseHelper.COLUMN_PASSWORD, "123456")
        }
        db.insert(DatabaseHelper.TABLE_USERS, null, values)
    }


    @SuppressLint("Range")
    private fun getContacts() {
        val contactList = StringBuilder()

        val resolver: ContentResolver = contentResolver
        val cursor: Cursor? = resolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                contactList.append("Name: $contactName\n")

                // Fetch phone numbers for each contact
                val phones = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(contactId),
                    null
                )
                if (phones != null && phones.moveToFirst()) {
                    do {
                        val phoneNumber = phones.getString(
                            phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        )
                        contactList.append("Phone: $phoneNumber\n")
                    } while (phones.moveToNext())
                    phones.close()
                }

                contactList.append("\n")
            }
            cursor.close()
            binding.contact.text = contactList.toString()
        } else {
            binding.contact.text = "No contacts found."
        }
    }
}
