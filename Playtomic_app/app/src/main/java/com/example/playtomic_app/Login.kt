package com.example.playtomic_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playtomic_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var loginBinding: ActivityLoginBinding
    private val auth = FirebaseAuth.getInstance()

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root
        setContentView(view)

        //Go to Login
        loginBinding.registerNow.setOnClickListener(){
            intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }

        //login
        loginBinding.btnLogin.setOnClickListener(){

            loginBinding.progressBar.visibility = View.VISIBLE;
            val email = loginBinding.email.text.toString()
            val password = loginBinding.password.text.toString()

            if (TextUtils.isEmpty(email)){
                loginBinding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)){
                loginBinding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show();
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    loginBinding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            this,
                            "Login Successful.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
        }

    }
}