package com.bangkit.collabolio.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bangkit.collabolio.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser

        Handler(Looper.getMainLooper()).postDelayed({
            if (currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }

            finish()
        }, 2000)
    }
}