package com.example.fchat

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SenderActivity : AppCompatActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var databaseReference: DatabaseReference
    private val firebaseAuth = FirebaseAuth.getInstance()

// CurrentUserId
    private val senderId = firebaseAuth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sender)

        val receiverId = intent.getStringExtra("userId")
        val userName = intent.getStringExtra("userName")
        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId
        // Retrieve user information from the chat Activity intent

        // ReceiverUser Name Display Top On SenderSide
        findViewById<TextView>(R.id.receiverUsernameTextView).text = userName?.uppercase() ?: "receiver"

//        PlaceHolder Where sending Data Tacking For Sending
        val messageEditText = findViewById<EditText>(R.id.messageEditText)
        val sendButton = findViewById<Button>(R.id.sendButton)
        val listViewSentChat: ListView = findViewById(R.id.listViewChat)

        val messages = mutableListOf<ChatMessage>()
        messageAdapter = MessageAdapter(this, messages)
        listViewSentChat.adapter = messageAdapter
// receives data from firebase and display in chat activity
        databaseReference = FirebaseDatabase.getInstance().getReference("messages")
        databaseReference.child(receiverRoom).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val userList = mutableListOf<UserModel>()
                val messageList = mutableListOf<ChatMessage>()
                // Iterate through the snapshot and add users to the list
                for (userSnapshot in snapshot.children) {
                    val senderId = userSnapshot.child("senderId").getValue(String::class.java)
                    val receiverId = userSnapshot.child("receiverId").getValue(String::class.java)
                    val message = userSnapshot.child("message").getValue(String::class.java)
                    if (receiverId != null && message != null) {
                        messageList.add(ChatMessage(senderId, receiverId, message))
                    }
                }
                // Update the adapter with the new user list
//                messageAdapter.clear()
                messageAdapter.addAll(messageList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()

            if (messageText.isNotEmpty() && senderId != null) {
                sendMessage(senderId, receiverId!!, messageText)

                messageEditText.text.clear()
            }
        }
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {
//        val messageId = databaseReference.push().key ?: return
//        val timestamp = System.currentTimeMillis()

        val chatMessage = ChatMessage(senderId, receiverId, message)
        // Add Message To the Database

        val senderRoom = senderId + receiverId
        val receiverRoom = receiverId + senderId

        databaseReference.child(senderRoom).child(senderId).setValue(chatMessage).addOnSuccessListener {
            databaseReference.child(receiverRoom).child(receiverId).setValue(chatMessage)
        }
//        messageAdapter.add(chatMessage)
        messageAdapter.notifyDataSetChanged()
    }
}
