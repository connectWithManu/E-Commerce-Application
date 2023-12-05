package com.manu.shopsyuser.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.R
import com.manu.shopsyuser.activities.HomeMainActivity
import com.manu.shopsyuser.databinding.ActivityWelcomeBinding
import com.manu.shopsyuser.utils.DefaultFunction
import com.manu.shopsyuser.utils.Objects

class WelcomeActivity : AppCompatActivity() {
    private val binding by lazy { ActivityWelcomeBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btLaunchCreateAc.setOnClickListener {
            startActivity(Intent(this@WelcomeActivity, RegisterActivity::class.java))
        }

        binding.btLogin.setOnClickListener {
            login()
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            email.isEmpty() -> {
                binding.etEmail.requestFocus()
                binding.etEmail.error = "Empty"
                false
            }

            password.isEmpty() -> {
                binding.etPassword.requestFocus()
                binding.etPassword.error = "Empty"
                false
            }

            else -> {
                true
            }
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString().trim()
        if (validateForm(email, password)) {
            DefaultFunction.loading(this)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        startActivity(Intent(this@WelcomeActivity, HomeMainActivity::class.java))
                        finish()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this@WelcomeActivity, it.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }

        }
    }
}