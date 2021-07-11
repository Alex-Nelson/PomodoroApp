package com.example.pomodoroapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * The [Entity] represents a task in a Pomodoro.
 * */
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var taskId: Long = 0L,

    @ColumnInfo(name = "task_name")
    var taskName: String,

    @ColumnInfo(name = "session_length")
    var sessionLength: Int,

    @ColumnInfo(name = "short_break_length")
    var shortBreakLength: Int,

    @ColumnInfo(name = "long_break_length")
    var longBreakLength: Int,

    @ColumnInfo(name = "number_of_sessions")
    var numberOfSessions: Int
)