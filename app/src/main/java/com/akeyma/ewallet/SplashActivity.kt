package com.akeyma.ewallet

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.akeyma.ewallet.auth.LoginActivity
import com.akeyma.ewallet.databinding.ActivitySplashBinding
import com.akeyma.ewallet.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        val logo = ObjectAnimator.ofFloat(binding.imgLogo, "alpha", 0f, 1f).apply {
            duration = 600
        }
        val name = ObjectAnimator.ofFloat(binding.tvAppName, "alpha", 0f, 1f).apply {
            duration = 600
            startDelay = 300
        }
        val sub = ObjectAnimator.ofFloat(binding.tvSubtitle, "alpha", 0f, 1f).apply {
            duration = 600
            startDelay = 500
        }
        val tagline = ObjectAnimator.ofFloat(binding.tvTagline, "alpha", 0f, 1f).apply {
            duration = 600
            startDelay = 700
        }

        AnimatorSet().apply {
            playTogether(logo, name, sub, tagline)
            start()
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (sessionManager.isLoggedIn()) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 2500)
    }
}