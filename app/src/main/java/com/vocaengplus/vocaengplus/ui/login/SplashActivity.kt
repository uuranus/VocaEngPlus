package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.vocaengplus.vocaengplus.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val handler=Handler()
        handler.postDelayed(Runnable {
            val intent= Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        },3000)

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}