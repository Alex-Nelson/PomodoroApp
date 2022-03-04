package com.example.pomodorotimerapplication.utilities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.pomodorotimerapplication.notifications.TimerExpiredReceiver
import kotlin.properties.Delegates

class TimerUtils {

    /**
     * Inner enum class for the states of a timer
     * */
    enum class TimerState {
        Stopped, Paused, Running
    }

    /**
     * Inner enum class for the timer type
     * */
    enum class TimerType {
        Session, ShortBreak, LongBreak, Finished
    }

    companion object {
        // Time when the timer is done
        const val DONE = 0L

        // Countdown time interval
        const val ONE_SECOND = 1000L

        var timeValue by Delegates.notNull<Long>()

        fun setNowSeconds(time: Long) {
            timeValue = time
        }

        /**
         * Set an alarm to notify user the timer is done.
         *
         * @param context
         * @param nowSeconds
         * @param timeLeft
         *
         * @return
         * */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun setAlarm(context: Context, nowSeconds: Long, timeLeft: Long) : Long {
            val timerDone = (nowSeconds + timeLeft) * ONE_SECOND
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            // RTC_WAKEUP wakes up device if it's sleeping
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timerDone, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return timerDone
        }

        /**
         * Removes the alarm for the notification
         * */
        @SuppressLint("UnspecifiedImmutableFlag")
        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.cancel(pendingIntent)
        }
    }

}