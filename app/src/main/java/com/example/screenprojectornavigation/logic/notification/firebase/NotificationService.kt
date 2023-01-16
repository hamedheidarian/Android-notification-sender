package com.example.screenprojectornavigation.logic.notification.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.example.screenprojectornavigation.logic.notification.Utils
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.logging.Logger

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.getData().size > 0) {
            val CHANNEL_ID = "my_channel_01"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mNotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel =
                    NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance)
                mNotificationManager.createNotificationChannel(mChannel)
            }
        }
    }

    override fun onNewToken(refreshedToken: String) {
        println("token")
        Log.d("Registration ID", "Refreshed token: $refreshedToken")
            Utils.setToken(refreshedToken,application)
    }
}
