package com.manu.shopsyuser

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.manu.shopsyuser.activities.HomeMainActivity
import com.manu.shopsyuser.authentication.WelcomeActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            if(FirebaseAuth.getInstance().currentUser != null) {
                startActivity(Intent(this@MainActivity, HomeMainActivity::class.java))
            } else {
                startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            }
        }, 2000)
    }
}