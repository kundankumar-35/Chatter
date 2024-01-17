package com.example.fchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Handle login  button click
        findViewById<Button>(R.id.signUp).setOnClickListener {
            val uName = findViewById<EditText>(R.id.name)
            val uEmail = findViewById<EditText>(R.id.emailEditText)
            val uPassword = findViewById<EditText>(R.id.passwordEditText)
            val name = uName.text.toString().trim()
            val email = uEmail.text.toString().trim()
            val password = uPassword.text.toString().trim()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                signUpUser(name, email, password)
                uName.text?.clear()
                uEmail.text?.clear()
                uPassword.text?.clear()
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUpUser(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    database = FirebaseDatabase.getInstance().getReference("AllUsers")
                    val userId = auth.currentUser!!.uid
                    val newUser = NewUser(userId, name, email, password)

                    database.child(name).setValue(newUser).addOnSuccessListener {
                        Toast.makeText(this, "User register", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ChatActivity::class.java))
                    }.addOnCanceledListener {
                        Toast.makeText(this, "Failed Registration", Toast.LENGTH_SHORT).show()
                    }
//
                }
            }
    }
}
