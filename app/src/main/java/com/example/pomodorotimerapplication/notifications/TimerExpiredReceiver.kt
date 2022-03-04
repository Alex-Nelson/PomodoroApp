package com.example.pomodorotimerapplication.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.pomodorotimerapplication.utilities.PrefUtil
import com.example.pomodorotimerapplication.utilities.TimerUtils

/**
 *
 * */
class TimerExpiredReceiver : BroadcastReceiver() {

    /**
     * Called when the Broadcast Receiver is receiving an Intent broadcast
     * */
    override fun onReceive(context: Context, intent: Intent) {
        TimerNotifications.showTimerExpired(context)

        PrefUtil.setTimerState(TimerUtils.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0L, context)
    }
}