package com.awkitsune.tictactoegame

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.os.Build

import android.graphics.ImageDecoder
import java.lang.Exception


class AuthActivity : AppCompatActivity(), CoroutineScope {
    private val jobSaveUserdata = Job()

    private lateinit var settings: SharedPreferences
    private var user = User()

    private val selectImageFromGalleryResult =
        registerForActivityResult( ActivityResultContracts.GetContent() ) { uri: Uri? ->
            uri?.let {
                val avLoading = findViewById<ProgressBar>(R.id.progressBarAvatarLoading)
                launch {
                    avLoading.post {
                        avLoading.visibility = View.VISIBLE
                    }
                    var avatar: Bitmap? = null
                    val contentResolver = contentResolver

                    try {
                        avatar = if (Build.VERSION.SDK_INT < 28) {
                            MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        } else {
                            val source: ImageDecoder.Source =
                                ImageDecoder.createSource(contentResolver, uri)
                            ImageDecoder.decodeBitmap(source)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    val resizedAvatar =
                        Bitmap.createScaledBitmap(
                            avatar!!,
                            256, 256,
                            true
                        )

                    previewImage.post{
                        previewImage.setImageBitmap(resizedAvatar)
                    }

                    avLoading.post {
                        avLoading.visibility = View.GONE
                    }
                }
            }
    }

    private val previewImage by lazy { findViewById<ImageView>(R.id.imageViewAvatar) }

    private fun setOnClickListeners(){
        findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            launch {
                val textInputUsername = findViewById<EditText>(R.id.textFieldUsername)

                if (textInputUsername.length() > 0){
                    user.username = textInputUsername.text.toString()


                    user.avatarEncoded = Utilities.encodeToBase64(
                        findViewById<ImageView>(R.id.imageViewAvatar)
                            .drawable.toBitmap()).toString()

                    val userJson = Gson().toJson(user)

                    settings.edit()
                        .putString(
                            Constants.USER_PARCELABLE_KEY,
                            userJson
                        )
                        .putBoolean(
                            Constants.FIRST_LAUNCH_KEY,
                            false
                        )
                        .apply()

                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    Snackbar.make(
                        findViewById(R.id.loginConstraint),
                        R.string.warning_fill_all,
                        Snackbar.LENGTH_LONG
                    ).setAnchorView(findViewById(R.id.buttonLogin)).show()
                }
            }
        }

        findViewById<ImageButton>(R.id.imageButtonSelectAvatar).setOnClickListener{
            selectImageFromGallery()

            findViewById<ImageButton>(R.id.imageButtonClearAvatar).post {
                findViewById<ImageButton>(
                    R.id.imageButtonClearAvatar
                ).visibility = View.VISIBLE
            }

        }

        findViewById<ImageButton>(R.id.imageButtonClearAvatar).setOnClickListener{
            previewImage.setImageResource(R.drawable.ic_baseline_person_96)
            findViewById<ImageButton>(R.id.imageButtonClearAvatar).visibility = View.GONE
        }
    }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        settings = getSharedPreferences(Constants.ACCOUNT_PREFS_FILE, MODE_PRIVATE)

        setOnClickListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(Constants.USER_PARCELABLE_KEY, user)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        user = savedInstanceState.getParcelable(Constants.USER_PARCELABLE_KEY)!!
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + jobSaveUserdata


}