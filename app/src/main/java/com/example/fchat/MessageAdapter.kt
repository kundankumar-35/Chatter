package com.example.fchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(context: Context, messages: List<ChatMessage>) :
    ArrayAdapter<ChatMessage>(context, 0, messages) {
    private val currentUserId: String? = FirebaseAuth.getInstance().currentUser?.uid

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val message = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            if (message?.receiverId == currentUserId) {
                R.layout.item_message_received
            } else {
                R.layout.item_message_sent
            },
            parent,
            false,
        )

        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        messageTextView.text = message?.message

        return view
    }

//    fun add(messages: MutableList<ChatMessage>) {
//    }
}
