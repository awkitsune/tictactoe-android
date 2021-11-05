package com.awkitsune.tictactoegame

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.constraintlayout.widget.ConstraintLayout

class SplashActivity : AppCompatActivity() {
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        scheduleSplashScreen()
    }

    private fun scheduleSplashScreen(){
        val splashDuration = 1000L
        Handler(Looper.getMainLooper()).postDelayed({
            settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

            if(settings.contains(Constants.FIRST_LAUNCH_KEY) && !settings.getBoolean(Constants.FIRST_LAUNCH_KEY, false)){
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                settings.edit().putBoolean(Constants.FIRST_LAUNCH_KEY, true).apply()
                val intent = Intent(this@SplashActivity, AuthActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            this.finish()
        }, splashDuration)
    }
}