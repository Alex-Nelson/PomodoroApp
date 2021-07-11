package com.example.pomodoroapp.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        TimerNotifications.showTimerExpired(context)

        PrefUtil.setTimerState(TimerUtil.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}