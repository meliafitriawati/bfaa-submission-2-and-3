package com.example.githubuserapi.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuserapi.R
import com.example.githubuserapi.view.main.UsersActivity

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(mainLooper).postDelayed({
            val intent = Intent(this@SplashScreenActivity, UsersActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}