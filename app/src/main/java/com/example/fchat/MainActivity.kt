package com.example.fchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.signUp).setOnClickListener {
            startActivity(Intent(this, login::class.java))
        }

        auth = FirebaseAuth.getInstance()
        findViewById<Button>(R.id.signIn).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText)
            val pass = findViewById<EditText>(R.id.passwordEditText)
            val uemail = email.text.toString().trim()
            val upass = pass.text.toString().trim()
            if (uemail.isNotEmpty() && upass.isNotEmpty()) {
                loginUser(uemail, upass)
                email.text?.clear()
                pass.text?.clear()
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(uemail: String, upass: String) {
        auth.signInWithEmailAndPassword(uemail, upass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful
                    Log.d("LoginActivity", "signInWithEmail:success")
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    // You can navigate to the next activity or perform other actions here
                    startActivity(Intent(this, ChatActivity::class.java))
                } else {
                    Log.w("LoginActivity", "signupInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
//                    // If login  fails, display a message to the user.
                }
            }
            .addOnCanceledListener {
                Toast.makeText(this, "login failed!!", Toast.LENGTH_SHORT).show()
            }
    }
}
