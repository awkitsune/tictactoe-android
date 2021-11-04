package com.awkitsune.tictactoegame

import android.content.ContentResolver
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.CancellationSignal
import android.util.Size
import java.io.File
import java.net.URI


class Utilities {
    companion object{
        fun encodeToBase64(image: Bitmap): String? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)

            val b: ByteArray = baos.toByteArray()

            val imageEncoded: String = Base64.encodeToString(b, Base64.DEFAULT)

            Log.d("${Constants.UTILITIES_TAG}: Image encoded: \n", imageEncoded)
            return imageEncoded
        }
        fun decodeBase64(input: String?): Bitmap? {
            val decodedByte = Base64.decode(input, 0)
            Log.d("${Constants.UTILITIES_TAG}: Image decoded: \n", input!!)
            return BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.size)
        }

        fun normalize(value: Float, min: Float, max: Float) = (value - min) / (max - min)
    }
}