package com.example.pomodorotimerapplication.utilities

import android.content.Context
import android.preference.PreferenceManager

class PrefUtil {
    companion object {
        /**
         * Retrieve the timer length based on the timer type
         * */
        private const val TIMER_LENGTH_ID =
            "com.example.pomodorotimerapplication.viewmodel.runtimerviewmodel.currentTimer"
        fun getTimerLength(
            context: Context,
            type: String
        ): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)

            return when(type) {
                "Session" -> preferences.getInt(TIMER_LENGTH_ID, 25)
                "Short" -> preferences.getInt(TIMER_LENGTH_ID, 5)
                "Long" -> preferences.getInt(TIMER_LENGTH_ID, 15)
                else -> preferences.getInt(TIMER_LENGTH_ID, 0) // Finished
            }
        }

        /**
         * Save/Retrieve the previous timer's length
         * */
        // TODO: may change to a different variable in view model
        private const val PREVIOUS_TIMER_LENGTH_ID =
            "com.example.pomodorotimerapplication.viewmodel.runtimerviewmodel.currentTimer"
        fun getPreviousTimerLength(
            context: Context
        ): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_ID, 0)
        }

        fun setPreviousTimerLength(
            seconds: Long,
            context: Context
        ) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_ID, seconds)
            editor.apply()
        }

        /**
         * Save/Retrieve the timer's state
         * */
        private const val TIMER_STATE_ID =
            "com.example.pomodorotimerapplication.viewmodel.runtimerviewmodel.currentTimer"
        fun getTimerState(
            context: Context
        ): TimerUtils.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerUtils.TimerState.values()[ordinal]
        }

        fun setTimerState(
            state: TimerUtils.TimerState,
            context: Context
        ) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

        /**
         * Save/Retrieve the time remaining on the timer
         * */
        private const val SECONDS_REMAINING_ID =
            "com.example.pomodorotimerapplication.viewmodel.runtimerviewmodel.currentTimer"
        fun getSecondsRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME_ID =
            "com.example.pomodorotimerapplication.utilities.timerutils.backgrounded_time"
        fun getAlarmSetTime(context: Context): Long{
            val preference = PreferenceManager.getDefaultSharedPreferences(context)
            return preference.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}