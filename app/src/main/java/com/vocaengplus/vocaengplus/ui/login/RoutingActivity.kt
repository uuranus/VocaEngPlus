package com.vocaengplus.vocaengplus.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.vocaengplus.vocaengplus.network.auth.AuthService
import kotlinx.coroutines.launch

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