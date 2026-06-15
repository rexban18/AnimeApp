package com.aliexplainer.app.ui.splash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AccelerateDecelerateInterpolator
import com.aliexplainer.app.databinding.ActivitySplashBinding
import com.aliexplainer.app.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logoText.apply {
            alpha = 0f
            translationY = 100f
        }
        binding.subtitleText.apply {
            alpha = 0f
            translationY = 50f
        }

        val logoAnim = ObjectAnimator.ofFloat(binding.logoText, "alpha", 0f, 1f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }
        val logoTranslate = ObjectAnimator.ofFloat(binding.logoText, "translationY", 100f, 0f).apply {
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }
        val subtitleAnim = ObjectAnimator.ofFloat(binding.subtitleText, "alpha", 0f, 1f).apply {
            duration = 600
            startDelay = 400
            interpolator = AccelerateDecelerateInterpolator()
        }
        val subtitleTranslate = ObjectAnimator.ofFloat(binding.subtitleText, "translationY", 50f, 0f).apply {
            duration = 600
            startDelay = 400
            interpolator = AccelerateDecelerateInterpolator()
        }

        logoAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                binding.logoText.visibility = android.view.View.VISIBLE
            }
        })

        logoAnim.start()
        logoTranslate.start()
        subtitleAnim.start()
        subtitleTranslate.start()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 2000)
    }
}
