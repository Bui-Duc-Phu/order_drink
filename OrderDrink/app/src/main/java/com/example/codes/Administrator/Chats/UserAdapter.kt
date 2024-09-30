package com.example.codes.Administrator.Chats

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import com.example.codes.Models.Users
import com.example.codes.databinding.UserAdapterBinding


class UserAdapter : RecyclerView.Adapter<UserAdapter.viewholer>{
    lateinit var binding : UserAdapterBinding
    val content:Context
    val list : ArrayList<Users>

    constructor(content: Context, list: ArrayList<Users>) {
        this.content = content
        this.list = list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholer {
       binding = UserAdapterBinding.inflate(LayoutInflater.from(content),parent,false)
        return viewholer(binding.root)
    }
    override fun onBindViewHolder(holder: viewholer, position: Int) {
        holder.apply {
            val model = list[position]
            holder.userName.text = model.userName

            holder.layoutUser.setOnClickListener {
                val intent = Intent(content, ChatMainAdmin::class.java)
                intent.putExtra("userId",model.userID)
                intent.putExtra("userName",model.userName)
                content.startActivity(intent)
            }
        }
    }
    override fun getItemCount(): Int {
       return  list.size
    }
    inner class viewholer(view:View):RecyclerView.ViewHolder(view){
        val userName = binding.nameUser
        val layoutUser = binding.layoutUserItem
    }


}
