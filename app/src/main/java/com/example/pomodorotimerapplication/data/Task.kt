package com.example.pomodorotimerapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * An [Entity] representing a pomodoro task
 * */
@Entity(tableName = "task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val taskId: Long = 0L,

    @ColumnInfo(name = "task_name")
    var taskName: String,

    @ColumnInfo(name = "session_length")
    var sessionLen: Int,

    @ColumnInfo(name = "short_break_length")
    var shortBreakLen: Int,

    @ColumnInfo(name = "long_break_length")
    var longBreakLen: Int,

    @ColumnInfo(name = "session_number")
    var numOfSessions: Int,

    @ColumnInfo(name = "task_status")
    var taskStatus: String = ""
)