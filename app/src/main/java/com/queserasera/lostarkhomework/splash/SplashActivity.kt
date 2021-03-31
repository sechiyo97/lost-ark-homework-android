package com.queserasera.lostarkhomework.splash

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.queserasera.lostarkhomework.R
import com.queserasera.lostarkhomework.databinding.ActivitySplashBinding
import com.queserasera.lostarkhomework.main.MainActivity

class SplashActivity : AppCompatActivity() {
    private var appData: SharedPreferences? = null
    private var binding: ActivitySplashBinding? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)
        Handler(mainLooper).postDelayed({ showMain() }, 1000)
    }

    private fun showMain() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, R.anim.fadeout)
    }
}