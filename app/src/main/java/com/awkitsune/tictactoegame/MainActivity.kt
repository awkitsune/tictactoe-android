package com.awkitsune.tictactoegame

import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private lateinit var settings: SharedPreferences

    private var user = User()

    private val avatarImage by lazy { findViewById<ImageView>(R.id.imageViewAvatar) }

    private fun setDataIntoViews() {
        findViewById<ImageView>(R.id.imageViewAvatar).setImageBitmap(
            Utilities.decodeBase64(user.avatarEncoded)
        )
        findViewById<TextView>(R.id.textViewUsername).text =
            user.username

        updateScores()
    }

    private fun updateScores() {
        findViewById<TextView>(R.id.textViewGamesStats).text =
            "${getString(R.string.won_lost_string)} ${user.gamesWon}/${user.gamesLost}"
        findViewById<TextView>(R.id.textViewGamesAmount).text =
            "${getString(R.string.games_string)} ${user.gamesAmount}"
    }

    private fun setOnClickListeners(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.bottomAppBarGame))

        settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

        user = Gson().fromJson(
            settings.getString(Constants.USER_PARCELABLE_KEY, null),
            User::class.java
        )

        setDataIntoViews()

        setOnClickListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottomappbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item!!.itemId){
            R.id.app_bar_about ->
                Toast.makeText(
                    applicationContext,
                    "TODO open about activity",
                    Toast.LENGTH_SHORT)
                    .show()
        }
        return true
    }
}