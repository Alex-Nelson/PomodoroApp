package com.example.pomodorotimerapplication.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.pomodorotimerapplication.R

/**
 * A [Class] for the other notifications.
 *
 *
 * */
class Notifications {

    companion object {
        var isChannelCreated = false

        /**
         * Creates a notification channel for Android O and up
         * */
        @TargetApi(26)
        fun createChannel(
            context: Context,
            channelId: String,
            channelName: String,
            playSound: Boolean
        ) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Set up the channel's importance
                val channelImportance = if(playSound) NotificationManager.IMPORTANCE_DEFAULT
                    else NotificationManager.IMPORTANCE_LOW

                val mChannel = NotificationChannel(channelId, channelName, channelImportance)
                mChannel.lightColor = ContextCompat.getColor(context, R.color.green_dark)

                val notificationManager = context.getSystemService(
                    Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(mChannel)
                isChannelCreated = true
            }
        }

        /**
         * Notify the user of the status of the timer if a channel exists
         * */
        fun notifyUser(
            context: Context,
            contentTitle: String,
            contentText: String?,
            channelId: String
        ) {
            if(isChannelCreated) {
                val mBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(contentTitle)

                // Add content text if not null
                if(contentText != null) {
                    mBuilder.setContentText(contentText)
                }

                val notification = mBuilder.build()
                val notificationManagerCompat = NotificationManagerCompat.from(context)
                notificationManagerCompat.notify(0, notification)
            }
        }

        /**
         * Hide the notification when the app is in focus
         * */
        fun hideNotification(
            context: Context
        ) {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.cancel(0)
        }
    }
}