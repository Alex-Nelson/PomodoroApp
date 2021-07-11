package com.example.pomodoroapp.util

import android.content.Context
import android.preference.PreferenceManager

class PrefUtil {

    companion object {

        //        //private const val TIMER_LENGTH_ID = "com.resocoder.timer.timer_length"
//        /**
//         * These save the current
//         * */
//        private const val TIMER_LENGTH_ID =
//            "com.example.pomodoroapp.task.TaskViewModel.currentTime"
//
//        fun getTimerLength(context: Context): Int {
//            // TODO: See if there is an equivalent for deprecated function
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getInt(TIMER_LENGTH_ID, 10)
//        }
//
        /**
         * Retrieve the timer length based on the type of timer
         * */
        private const val TIMER_LENGTH_ID =
            "com.example.pomodoroapp.task.taskviewmodel.current_time"
        fun getTimerLength(context: Context, type: String): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return when(type){
                "Session" -> preferences.getInt(TIMER_LENGTH_ID, 25) // Session time
                "Short Break" -> preferences.getInt(TIMER_LENGTH_ID, 5) // Short break
                "Long Break" -> preferences.getInt(TIMER_LENGTH_ID, 15) // Long break
                else -> preferences.getInt(TIMER_LENGTH_ID, 0) // Finished
            }
        }

        //        /**
//         * These get
//         * */
//        private const val PREVIOUS_TIMER_LENGTH_ID =
//            "com.example.pomodoro.util.timerutil.previous_timer_length"
//        fun getPreviousTimerLength(context: Context): Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getLong(PREVIOUS_TIMER_LENGTH_ID, 0)
//        }
//
//        fun setPreviousTimerLength(seconds: Long, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(PREVIOUS_TIMER_LENGTH_ID, seconds)
//            editor.apply()
//        }
//
        /**
         * Save/Retrieve the previous timer's length
         * */
        private const val PREVIOUS_TIMER_LENGTH_ID =
            "com.example.pomodoroapp.task.taskviewmodel.timer_length"
        fun getPreviousTimerLength(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_ID, 0)
        }

        fun setPreviousTimerLength(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_ID, seconds)
            editor.apply()
        }

//        /**
//         * These save and retrieve the timer's state
//         * */
//        private const val TIMER_STATE_ID = "com.example.pomodoroapp.task.TaskViewModel.timerStatus"
//        fun getTimerState(context: Context): TimerUtil.TimerState{
//            val preference = PreferenceManager.getDefaultSharedPreferences(context)
//            val ordinal = preference.getInt(TIMER_STATE_ID, 0)
//            return TimerUtil.TimerState.values()[ordinal]
//        }
//
//        fun setTimerState(state: TimerUtil.TimerState, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            val ordinal = state.ordinal
//            editor.putInt(TIMER_STATE_ID, ordinal)
//            editor.apply()
//        }
//
        private const val TIMER_STATE_ID =
            "com.example.pomodoroapp.task.taskviewmodel.timer_state"
        fun getTimerState(context: Context): TimerUtil.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return TimerUtil.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerUtil.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }

//        /**
//         * These save and retrieve the time left on the timer
//         * */
//        private const val SECONDS_REMAINING_ID =
//            "com.example.pomodoroapp.util.timerutil.time_remaining"
//        fun getSecondsRemaining(context: Context): Long{
//            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
//            return preferences.getLong(SECONDS_REMAINING_ID, 0)
//        }
//
//        fun setSecondsRemaining(seconds: Long, context: Context){
//            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
//            editor.putLong(SECONDS_REMAINING_ID, seconds)
//            editor.apply()
//        }

        /**
         * Save/Retrieve the time remaining on the timer
         * */
        private const val SECONDS_REMAINING_ID =
            "com.example.pomodoro.task.taskviewmodel.current_time"
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
            "com.example.pomodoroapp.util.timerutil.backgrounded_time"
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