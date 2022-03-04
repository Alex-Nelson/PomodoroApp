package com.example.pomodorotimerapplication.data

import androidx.room.*

/**
 * An [Entity] representing the most recent timer of a pomodoro task.
 * */
@Entity(tableName = "timer_table")
data class RecentTimer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "timerId")
    val timerId: Long = 0L,

    // Foreign key from task table
    @ColumnInfo(name = "taskId")
    val taskId: Long,

    @ColumnInfo(name = "timer_type")
    var type: String,

    @ColumnInfo(name = "time_left")
    var timeLeft: Long,

    @ColumnInfo(name = "timer_length")
    var timerLen: Long,

    @ColumnInfo(name = "sessions_ran")
    var sessionsRan: Int
)

/**
 * The relationship between a task and its last timer
 * */
data class TaskAndTimer(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val recentTimer: RecentTimer
)
