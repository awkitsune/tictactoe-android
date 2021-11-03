package com.awkitsune.tictactoegame

import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var settings: SharedPreferences

    private val avatarImage by lazy { findViewById<ImageView>(R.id.imageViewAvatar) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

        findViewById<TextView>(R.id.textViewUsername).text =
            settings.getString(Constants.ACCOUNT_USERNAME_KEY, "undefined")

        findViewById<ImageView>(R.id.imageViewAvatar).setImageBitmap(Utilities.decodeBase64(
            settings.getString(Constants.ACCOUNT_IMAGE_KEY, "")
        ))
    }
}