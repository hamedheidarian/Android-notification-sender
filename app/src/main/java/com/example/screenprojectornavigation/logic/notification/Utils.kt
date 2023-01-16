package com.example.screenprojectornavigation.logic.notification

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import kotlin.math.roundToInt

class Utils {
    companion object {
        const val GOOGLE_TOKEN_KEY = "google_token_id"

        fun getToken(application: Application): String? {
            val sharedPref: SharedPreferences = application.applicationContext.getSharedPreferences(
                "cps", MODE_PRIVATE)
            return sharedPref.getString(GOOGLE_TOKEN_KEY, "")
        }

        fun setToken(token: String?, application: Application) {
            val sharedPref: SharedPreferences = application.applicationContext.getSharedPreferences("cps", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString(GOOGLE_TOKEN_KEY, token)
            editor.apply()
        }
    }
}