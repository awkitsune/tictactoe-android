package com.awkitsune.tictactoegame

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var settings: SharedPreferences

    private var user = User()

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
        findViewById<ImageView>(R.id.f00).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f10).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f20).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f01).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f11).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f21).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f02).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f12).setOnClickListener { fieldClicked() }
        findViewById<ImageView>(R.id.f22).setOnClickListener { fieldClicked() }

        findViewById<FloatingActionButton>(R.id.floatingActionButtonPlayRestart)
            .setOnClickListener {

            }
    }

    private fun fieldClicked(){

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
        when (item.itemId){
            R.id.app_bar_about -> {
                val aboutAlertDialog = LayoutInflater.from(this)
                    .inflate(R.layout.dialog_about, null, false)

                MaterialAlertDialogBuilder(this)
                    .setView(aboutAlertDialog)
                    .setPositiveButton(android.R.string.ok) {_, _ -> }
                    .show()
            }

            R.id.app_bar_logout -> {
                MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.dialog_logout_message)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialog, id ->
                        settings.edit()
                            .putBoolean(Constants.FIRST_LAUNCH_KEY, true)
                            .apply()

                        startActivity(
                            Intent(this@MainActivity, AuthActivity::class.java)
                        )
                        finish()
                    }
                    .setNegativeButton(
                        android.R.string.cancel
                    ) { _, _ -> }
                    .setIcon(R.drawable.ic_baseline_delete_forever_24)
                    .show()
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

    }
}