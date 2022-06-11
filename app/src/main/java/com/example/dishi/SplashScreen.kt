package com.example.dishi

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AnimationUtils
import com.example.dishi.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //set viewBinding
        val splashScreenBinding: ActivitySplashScreenBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashScreenBinding.root)

       // make the splashscreen cover the whole screen
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashlogoAnimation=AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        val splashLogoTextAnimation = AnimationUtils.loadAnimation(this,R.anim.splash_ltext_anim)
        splashScreenBinding.logoImage.animation = splashlogoAnimation
        splashScreenBinding.textvAppName.animation = splashLogoTextAnimation
    }
}