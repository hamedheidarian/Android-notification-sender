package com.example.screenprojectornavigation

import android.Manifest.permission
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

const val NOTIFICATION_LISTENER_PERMISSION_REQUEST_CODE = 0

class NotificationActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!isNotificationServiceEnabled()) {
                ActivityCompat.requestPermissions(this, arrayOf(permission.
                BIND_NOTIFICATION_LISTENER_SERVICE), NOTIFICATION_LISTENER_PERMISSION_REQUEST_CODE)
        }
        else
            finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_LISTENER_PERMISSION_REQUEST_CODE -> {
                if (!(grantResults.isNotEmpty()
                                && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startActivity( Intent(applicationContext.getString(R.string
                            .notification_access_intent)));
                }
                finish()
            }
        }

    }

    private fun isNotificationServiceEnabled(): Boolean {
        val flat: String = Settings.Secure.getString(contentResolver,
                "enabled_notification_listeners")
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(this.packageName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }
}