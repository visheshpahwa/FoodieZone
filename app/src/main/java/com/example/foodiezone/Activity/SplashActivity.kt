package com.example.foodiezone.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.foodiezone.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        Handler().postDelayed({
            val intent= Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)

    }
}