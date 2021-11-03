package com.awkitsune.tictactoegame

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SharedPrefsDebugger : AppCompatActivity() {
    private lateinit var settings: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_prefs_debugger)

        settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

        findViewById<TextView>(R.id.textViewUsr).text =
            settings.getString(Constants.ACCOUNT_USERNAME_KEY, "undefined")

        findViewById<ImageView>(R.id.imageViewAv96).setImageBitmap(Utilities.decodeBase64(
            settings.getString(Constants.ACCOUNT_IMAGE_KEY, "")
        ))

        findViewById<ImageView>(R.id.imageViewAv192).setImageBitmap(Utilities.decodeBase64(
            settings.getString(Constants.ACCOUNT_IMAGE_KEY, "")
        ))
    }
}