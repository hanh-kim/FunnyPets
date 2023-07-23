package com.hpk.funnypet

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var shouldKeepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            shouldKeepSplash
        }
        super.onCreate(savedInstanceState)
        shouldKeepSplash = false
        lifecycleScope.launch {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}