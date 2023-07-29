package com.vocaengplus.vocaengplus.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.ui.login.LoginActivity

class RoutingActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }

        if (AuthService.getCurrentUID().isNotEmpty()) {
            startActivity(Intent(this@RoutingActivity, MainActivity::class.java))
        } else {
            startActivity(Intent(this@RoutingActivity, LoginActivity::class.java))
        }
        finish()

    }
}