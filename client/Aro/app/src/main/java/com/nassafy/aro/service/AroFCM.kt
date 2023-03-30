package com.nassafy.aro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nassafy.aro.R
import com.nassafy.aro.ui.view.main.MainActivity

private const val TAG = "AroFCM_싸피"

class AroFCM : FirebaseMessagingService() {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: Notification.Builder


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    } // End of onNewToken

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (remoteMessage.notification != null) {
            showNotifictaion(
                remoteMessage.notification!!.title.toString(),
                remoteMessage.notification!!.body.toString()
            )
        }
    } // End of onMessageReceived

    private fun showNotifictaion(title: String, messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)

        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel =
                NotificationChannel(
                    title, messageBody, NotificationManager.IMPORTANCE_HIGH
                )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor =
                ContextCompat.getColor(this, R.color.join_background_color)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, title)
                //.setContent(contentView)
                .setSmallIcon(R.drawable.tab_aurora_icon)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.tab_aurora_icon
                    )
                )
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                //.setContent(contentView)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.ic_launcher_foreground
                    )
                )
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(1234, builder.build())
    } // End of showNotifictaion

    fun getFirebaseToken(): String {
        var token = ""
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            token = it.toString()
        }

        return token
    } // End of getFirebaseToken
} // End of AroFCM class
