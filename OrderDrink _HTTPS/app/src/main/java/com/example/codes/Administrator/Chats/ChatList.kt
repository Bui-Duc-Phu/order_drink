package com.example.codes.Administrator.Chats

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codes.Models.Users
import com.example.codes.databinding.ActivityChatListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatList : AppCompatActivity() {
    private val binding: ActivityChatListBinding  by lazy {
        ActivityChatListBinding.inflate(layoutInflater)
    }
    lateinit var list: ArrayList<Users>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        list = ArrayList()



        binding.backBtn.setOnClickListener {
            onBackPressed()
        }


        getFirebaseUser()




    }

    private fun getFirebaseUser() {
        val ref = FirebaseDatabase.getInstance("https://coffe-app-19ec3-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users")
        ref.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (ds in snapshot.children) {
                    val user = ds.getValue(Users::class.java)
                    if (!user?.typeAccount.equals("2")) {
                        list.add(user!!)
                    }
                }
                val adapter = UserAdapter(this@ChatList, list)
                binding.recylerview.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
                System.out.println(error.message)
            }
        })
    }
}