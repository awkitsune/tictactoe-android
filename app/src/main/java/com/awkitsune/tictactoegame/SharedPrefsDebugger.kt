package com.awkitsune.tictactoegame

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson

class SharedPrefsDebugger : AppCompatActivity() {
    private lateinit var settings: SharedPreferences

    var user = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_prefs_debugger)

        settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

        user = Gson().fromJson(
            settings.getString(Constants.USER_PARCELABLE_KEY, null),
            User::class.java
        )

        findViewById<TextView>(R.id.textViewUData).text =
            settings.getString(Constants.USER_PARCELABLE_KEY, null)

        findViewById<ImageView>(R.id.imageViewAv96).setImageBitmap(
            Utilities.decodeBase64(user.avatarEncoded)
        )

        findViewById<TableRow>(R.id.tableRowUserdata).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData = ClipData.newPlainText(
                "tictactoe userdata copy",
                findViewById<TextView>(R.id.textViewUData).text)

            clipboard.setPrimaryClip(clip)

            Toast.makeText(
                applicationContext,
                getText(R.string.userdata_copy_string),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}