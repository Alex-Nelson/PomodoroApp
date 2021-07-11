package com.example.pomodoroapp.data

import androidx.room.*

@Entity(tableName = "timer_table")
data class Timer(
    @PrimaryKey(autoGenerate = true)
    var timerId: Long = 0L,

    @ColumnInfo(name = "timer_type")
    var type: String,

    @ColumnInfo(name = "timer_task_id")
    var timerTaskId: Long,

    @ColumnInfo(name = "timer_state")
    var state: Int,

    @ColumnInfo(name = "time_left")
    var timeLeft: Long,

    @ColumnInfo(name = "time_length")
    var timeLength: Long
)

/**
 * The relationship between a task and its timer
 * */
data class TaskAndTimer(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "taskId",
        entityColumn = "timer_task_id"
    )
    val timer: Timer
)
