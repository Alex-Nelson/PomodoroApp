package com.example.pomodoroapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimerNotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        when(intent.action){
            AppConstants.ACTION_STOP -> {
                TimerUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerUtil.TimerState.Stopped, context)
                TimerNotifications.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerUtil.timeValue

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                TimerUtil.removeAlarm(context)
                PrefUtil.setTimerState(TimerUtil.TimerState.Paused, context)
                TimerNotifications.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val timerDone = TimerUtil.setAlarm(context, TimerUtil.timeValue, secondsRemaining)
                PrefUtil.setTimerState(TimerUtil.TimerState.Running, context)
                TimerNotifications.showTimerRunning(context, timerDone)
            }
            else -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val timerDone = TimerUtil.setAlarm(context, TimerUtil.timeValue, secondsRemaining)

                PrefUtil.setTimerState(TimerUtil.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                TimerNotifications.showTimerRunning(context, timerDone)
            }
        }
    }
}