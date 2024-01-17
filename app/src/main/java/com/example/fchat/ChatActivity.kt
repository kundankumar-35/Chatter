package com.example.fchat

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var userAdapter: UserAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat2)
        listView = findViewById(R.id.listView)
        databaseReference = FirebaseDatabase.getInstance().getReference("AllUsers")
        // Initialize the UserAdapter
        userAdapter = UserAdapter(this, mutableListOf())
        // Set the adapter to the ListView
        listView.adapter = userAdapter
        val userList = mutableListOf<UserModel>()

        // Add a ValueEventListener to listen for changes in the data
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
//                val userList = mutableListOf<UserModel>()

                // Iterate through the snapshot and add users to the list
                for (userSnapshot in snapshot.children) {
                    val userId = userSnapshot.child("userId").getValue(String::class.java)
                    val username = userSnapshot.child("name").getValue(String::class.java)
                    if (userId != null && username != null) {
                        userList.add(UserModel(userId, username))
                    }
                }
                // Update the adapter with the new user list
                userAdapter.clear()
                userAdapter.addAll(userList)
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })

        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedPerson = userAdapter.getItem(position)
            val username = selectedPerson?.username
            val userid = selectedPerson?.userId
            // Start the ChatActivity with user information
            Toast.makeText(this, "Clicked on $username", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SenderActivity::class.java)
            intent.putExtra("userId", userid)
            intent.putExtra("userName", username)
            intent.putExtra("position", position)
            startActivity(intent)
        }
    }
}
