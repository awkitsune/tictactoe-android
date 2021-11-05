package com.awkitsune.tictactoegame

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private val jobSaveUserdata = Job()

    private lateinit var settings: SharedPreferences

    private var user = User()
    private var gameField = GameField()

    private fun setDataIntoViews() {
        launch {
            findViewById<ImageView>(R.id.imageViewAvatar).post {
                findViewById<ImageView>(R.id.imageViewAvatar).setImageBitmap(
                    Utilities.decodeBase64(user.avatarEncoded)
                )
            }
        }

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
        val tableGameField = findViewById<TableLayout>(R.id.tableGameField)


        findViewById<Button>(R.id.f00)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f00)) }
        findViewById<Button>(R.id.f10)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f10)) }
        findViewById<Button>(R.id.f20)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f20)) }
        findViewById<Button>(R.id.f01)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f01)) }
        findViewById<Button>(R.id.f11)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f11)) }
        findViewById<Button>(R.id.f21)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f21)) }
        findViewById<Button>(R.id.f02)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f02)) }
        findViewById<Button>(R.id.f12)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f12)) }
        findViewById<Button>(R.id.f22)
            .setOnClickListener { fieldClicked(findViewById<Button>(R.id.f22)) }

        findViewById<FloatingActionButton>(R.id.floatingActionButtonPlayRestart)
            .setOnClickListener {
                tableGameField.visibility = View.VISIBLE

                findViewById<FloatingActionButton>(R.id.floatingActionButtonPlayRestart)
                    .setImageResource(R.drawable.ic_baseline_refresh_24)

                updateScores()

                if (gameField.isWon) {
                    fieldRestart()
                } else {
                    MaterialAlertDialogBuilder(this)
                        .setMessage(getString(R.string.dialog_restart_message))
                        .setPositiveButton(android.R.string.ok) { _, _ -> fieldRestart() }
                        .setNegativeButton(android.R.string.cancel) { _, _ -> }
                        .show()
                }
            }
    }

    private fun fieldRestart() {
        user.gamesAmount++

        gameField.flush()
        updateFieldButtons(gameField.field)
        gameField.isWon = false
    }

    private fun winMessage() {
        if (gameField.whoWin() == GameField.playerMark) {
            Snackbar.make(
                findViewById(R.id.mainConstraint),
                "${ user.username } ${ getString(R.string.message_win) } ${ getString(R.string.string_ai_name) } ",
                Snackbar.LENGTH_LONG)
                .setAnchorView(findViewById(R.id.floatingActionButtonPlayRestart))
                .show()

            user.gamesWon++

            updateScores()
        }
        if (gameField.whoWin() == GameField.aiMark) {
            Snackbar.make(
                findViewById(R.id.mainConstraint),
                "${ getString(R.string.string_ai_name) } ${ getString(R.string.message_win) } ${ user.username } ",
                Snackbar.LENGTH_LONG)
                .setAnchorView(findViewById(R.id.floatingActionButtonPlayRestart))
                .show()

            user.gamesLost++

            updateScores()
        }
    }

    private fun fieldClicked(button: View){
        if (!gameField.isWon) {
            when (button.id) {
                R.id.f00 -> gameField.playerDraw(0, 0)
                R.id.f01 -> gameField.playerDraw(0, 1)
                R.id.f02 -> gameField.playerDraw(0, 2)
                R.id.f10 -> gameField.playerDraw(1, 0)
                R.id.f11 -> gameField.playerDraw(1, 1)
                R.id.f12 -> gameField.playerDraw(1, 2)
                R.id.f20 -> gameField.playerDraw(2, 0)
                R.id.f21 -> gameField.playerDraw(2, 1)
                R.id.f22 -> gameField.playerDraw(2, 2)
            }

            updateFieldButtons(gameField.field)
            winMessage()
        }
    }

    private fun updateFieldButtons(field: Array<Array<Int>>) {
        val icons = Array(3) { Array(3) { 0 } }

        for (i in 0..2) {
            for (j in 0..2) {
                    when (field[i][j]) {
                        GameField.playerMark ->
                            icons[i][j] = R.drawable.ic_baseline_clear_24
                        GameField.aiMark ->
                            icons[i][j] = R.drawable.ic_baseline_panorama_fish_eye_24
                    }
            }
        }

        findViewById<Button>(R.id.f00).setBackgroundResource( icons[0][0] )
        findViewById<Button>(R.id.f10).setBackgroundResource( icons[1][0] )
        findViewById<Button>(R.id.f20).setBackgroundResource( icons[2][0] )
        findViewById<Button>(R.id.f01).setBackgroundResource( icons[0][1] )
        findViewById<Button>(R.id.f11).setBackgroundResource( icons[1][1] )
        findViewById<Button>(R.id.f21).setBackgroundResource( icons[2][1] )
        findViewById<Button>(R.id.f02).setBackgroundResource( icons[0][2] )
        findViewById<Button>(R.id.f12).setBackgroundResource( icons[1][2] )
        findViewById<Button>(R.id.f22).setBackgroundResource( icons[2][2] )
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
                    .setNeutralButton(R.string.project_on_github) { _, _ ->
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.github_link)))
                        )
                    }
                    .setNeutralButtonIcon(getDrawable(R.drawable.ic_github_logo))
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

    override fun onPause() {
        launch {
            val userJson = Gson().toJson(user)

            settings.edit()
                .putString(
                    Constants.USER_PARCELABLE_KEY,
                    userJson
                )
                .apply()
        }

        super.onPause()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobSaveUserdata
}