package com.example.pomodoroapp.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import kotlin.properties.Delegates

/**
 * A util class to handle all the timer functionality
 * */
class TimerUtil {

    /**
     * Inner enum class for the states of a timer
     * */
    enum class TimerState{
        Stopped, Paused, Running
    }

    /**
     * Inner enum class for the timer type
     * */
    enum class TimerType{
        Session, ShortBreak, LongBreak, Finished
    }

    companion object {
        // Time when the timer is done
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L

        var timeValue by Delegates.notNull<Long>()

        /**
         *
         * */
        fun setNowSeconds(time: Long){
            timeValue = time
        }

        /**
         * Sets an alarm to let the user know the timer is done
         * */
        fun setAlarm(context: Context, nowSeconds: Long, timeLeft: Long): Long{
            val timerDone = (nowSeconds + timeLeft) * ONE_SECOND
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

            // RTC_WAKEUP wakes up the device if it's sleeping
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timerDone, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return timerDone
        }

        /**
         * Removes the alarm
         * */
        fun removeAlarm(context: Context){
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.cancel(pendingIntent)
        }
    }

    lateinit var timer: CountDownTimer
    var secondsRemaining = DONE
    var timerState = TimerState.Stopped
    var timerLength = DONE

    /**
     * Initialize the timer
     * */
    fun initTimer(timeValue: Long): CountDownTimer{
        timer = object : CountDownTimer(timeValue, ONE_SECOND) {

            // Decrement the timer's value by one second
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / ONE_SECOND
            }

            // When the timer reaches zero, decide which timer it will start next
            override fun onFinish() =  onTimerFinished()
        }

        return timer
    }

    /**
     *
     * */
    fun onTimerFinished(){
        timerState = TimerState.Stopped
        secondsRemaining = DONE


    }

}