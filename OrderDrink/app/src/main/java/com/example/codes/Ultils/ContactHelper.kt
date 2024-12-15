package com.example.codes.Ultils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract

class ContactHelper(private val context: Context) {

    @SuppressLint("Range")
    fun getContacts(): String {
        val contactList = StringBuilder()
        val resolver: ContentResolver = context.contentResolver

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
        } else {
            contactList.append("No contacts found.")
        }
        return contactList.toString()
    }
}
