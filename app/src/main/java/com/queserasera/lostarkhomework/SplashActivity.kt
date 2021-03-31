package com.queserasera.lostarkhomework

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private var handler = Handler(mainLooper)
    private var appData: SharedPreferences? = null

    /** Called when the activity is first created.  */
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // 설정값 불러오기
        appData = getSharedPreferences("appData", MODE_PRIVATE)
        handler.postDelayed({ showMain() }, 1000)
    }

    private fun showMain() {
        val intent = Intent(baseContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
        overridePendingTransition(0, R.anim.fadeout)
    }
}