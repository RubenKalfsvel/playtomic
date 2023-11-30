package com.example.playtomic_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playtomic_app.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {

    private lateinit var registerBinding: ActivityRegisterBinding
    private val auth = FirebaseAuth.getInstance()
    private val fStore =  Firebase.firestore
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
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = registerBinding.root
        setContentView(view)

        //Go to Login
        registerBinding.loginNow.setOnClickListener(){
            intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        //Create account
        registerBinding.btnRegister.setOnClickListener(){

            registerBinding.progressBar.visibility = View.VISIBLE;
            val email = registerBinding.email.text.toString()
            val password = registerBinding.password.text.toString()

            if (TextUtils.isEmpty(email)){
                registerBinding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(password)){
                registerBinding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Enter password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    registerBinding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this,"Account Created", Toast.LENGTH_SHORT).show()
                        val userID = auth.currentUser!!.uid
                        val user: MutableMap<String, Any> = mutableMapOf()
                        user["email"] = email
                        fStore.collection("users").document(userID)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("RegisterActivity", "Document successfully written")
                            }
                            .addOnFailureListener { e ->
                                // Handle error
                                Log.w("RegisterActivity", "Error writing document", e)
                            }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(
                            this,
                            "Account Creation Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }
    }
}