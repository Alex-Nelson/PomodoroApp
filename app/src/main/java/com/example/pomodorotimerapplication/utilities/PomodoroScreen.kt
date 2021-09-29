package com.example.pomodorotimerapplication.utilities

import androidx.compose.runtime.Composable

/**
 * Screen metadata for Pomodoro Timer App.
 * */
enum class PomodoroScreen(
    val body: @Composable ((String) -> Unit) -> Unit
) {
    Home(
        body = {}
    ),
    EditDialog(
        body = {}
    ),
    RunTimer(
        body = {}
    );

    @Composable
    fun Content(
        onScreenChange: (String) -> Unit
    ){
        body(onScreenChange)
    }

    companion object {
        fun fromRoute(route: String?): PomodoroScreen =
            when(route?.substringBefore("/")) {
                Home.name -> Home
                EditDialog.name -> EditDialog
                RunTimer.name -> RunTimer
                null -> Home
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}