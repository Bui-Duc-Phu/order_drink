package com.example.codes.Chats

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.codes.R
import com.example.codes.databinding.MessageItemLeftBinding
import com.example.codes.databinding.MessageItemRightBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ChatAdapter(val context: Context, val list: ArrayList<Chat>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1
    var firebaseUser: FirebaseUser? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_RIGHT) {
            val binding = MessageItemRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        } else {
            val binding = MessageItemLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ViewHolder(binding)
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        holder.bind(model)
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        return if (list[position].senderId == firebaseUser!!.uid) MESSAGE_TYPE_RIGHT else MESSAGE_TYPE_LEFT
    }

    inner class ViewHolder(private val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: Chat) {
            when (binding) {
                is MessageItemRightBinding ->{
                    binding.messageTV.text = chat.message
                }
                is MessageItemLeftBinding -> {
                    binding.messageTV.text = chat.message
                    binding.imageGchatProfileOther.setImageResource(R.drawable.adminimage)
                    binding.senderName.text = "Coffe Shop"
                }
                // Handle other bindings if necessary
            }
        }
    }
}