package com.example.pomodorotimerapplication.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pomodorotimerapplication.utilities.AppConstants
import com.example.pomodorotimerapplication.utilities.PrefUtil
import com.example.pomodorotimerapplication.utilities.TimerUtils

class TimerNotificationActionReceiver : BroadcastReceiver() {
    /**
     * Called when the Broadcast Receiver receives an Intent broadcast
     * */
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                // Stop the timer completely
                // TODO: remove the alarm
                PrefUtil.setTimerState(TimerUtils.TimerState.Stopped, context)
                // TODO: hide notification
            }
            AppConstants.ACTION_PAUSE -> {
                // Save the timer
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerUtils.timeValue

                secondsRemaining -= (nowSeconds - alarmSetTime)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                // TODO: remove the alarm
                PrefUtil.setTimerState(TimerUtils.TimerState.Paused, context)
                // TODO: show timer is paused
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val timerDone = TimerUtils.setAlarm(context, TimerUtils.timeValue, secondsRemaining)
                PrefUtil.setTimerState(TimerUtils.TimerState.Running, context)
                // TODO: Show timer is running
            }
            else -> {
                // TODO: pass in the correct timer type
                val minutesRemaining = PrefUtil.getTimerLength(context, type = "")
                val secondsRemaining = minutesRemaining * 60L
                val timerDone = TimerUtils.setAlarm(context, TimerUtils.timeValue, secondsRemaining)

                PrefUtil.setTimerState(TimerUtils.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                // TODO: Show timer is running
            }
        }
    }
}