package com.example.pomodorotimerapplication.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pomodorotimerapplication.R
import com.example.pomodorotimerapplication.utilities.AppConstants
import com.example.pomodorotimerapplication.utilities.TimerUtils

/**
 * A [Class] for the timer notifications.
 *
 * The notifications will display the timer currently being run (the user will be able to
 * start/pause the timer and cancel it. It will change the notification content when the timer is
 * done.
 * */
class TimerNotifications {

    companion object {
        private const val TIMER_ID = 0
        private const val CHANNEL_ID = "0"
        private const val CHANNEL_NAME = "TIMER_EVENT_CHANNEL"

        /**
         * Show a notification for when the timer is done
         * */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun showTimerExpired(context: Context) {
            // Set up the intents to allow the user to start the next timer when timer is done
            val startIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(
                context, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Create/Update the notification
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentText(context.getString(R.string.work_string))
                .addAction(R.drawable.ic_play, "Start", startPendingIntent)

            Notifications.createChannel(context, CHANNEL_ID, CHANNEL_NAME, true)

            val notification = mBuilder.build()
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(TIMER_ID, notification)
        }

        /**
         * Show a notification that shows the timer running
         * */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun showTimerRunning(context: Context, timerDone: Long) {
            // Set up intents to allow user to stop the timer
            val stopIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            stopIntent.action = AppConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(
                context, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Set up intents to allow user to pause the timer
            val pauseIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            pauseIntent.action = AppConstants.ACTION_PAUSE
            val pausePendingIntent = PendingIntent.getBroadcast(
                context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Create/Update the notification
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.work_string))
                .setContentText(context.getString(
                    R.string.time_left, DateUtils.formatElapsedTime(timerDone)))
                .setContentIntent(getPendingIntentWithStacks(context, TimerUtils::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_stop, "Stop", stopPendingIntent)
                .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)

            Notifications.createChannel(context, CHANNEL_ID, CHANNEL_NAME, true)

            val notification = mBuilder.build()
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(TIMER_ID, notification)
        }

        /**
         * Show a notification that shows the timer paused
         * */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun showTimerPaused(context: Context){
            // Set up intents to allow user to resume timer
            val resumeIntent = Intent(context, TimerNotificationActionReceiver::class.java)
            resumeIntent.action = AppConstants.ACTION_RESUME
            val resumePendingIntent = PendingIntent.getBroadcast(
                context, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Create/Update the notification
            val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(context.getString(R.string.work_string))
                .setOngoing(true)
                .addAction(R.drawable.ic_play, "Resume", resumePendingIntent)

            Notifications.createChannel(context, CHANNEL_ID, CHANNEL_NAME, true)

            val notification = mBuilder.build()
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.notify(TIMER_ID, notification)
        }

        /**
         * Hide the timer notification when the app is on screen
         * */
        fun hideTimerNotification(context: Context){
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            notificationManagerCompat.cancel(TIMER_ID)
        }

        private fun <T> getPendingIntentWithStacks(
            context: Context, javaClass: Class<T>): PendingIntent{

            val resultIntent = Intent(context, javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)

            return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}