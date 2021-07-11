package com.example.pomodoroapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * The DAO (Data Access Object) for the Task entity.
 * */
@Dao
interface TaskDao {

    /** Insert a task into the table */
    @Insert
    suspend fun insert(task: Task)

    /** Update the task */
    @Update
    suspend fun update(task: Task)

    /** Select a task from the table with the corresponding id */
    @Query("SELECT * FROM task_table WHERE taskId = :key")
    suspend fun getTask(key: Long): Task

    /** Select the most recently added task */
    @Query("SELECT * FROM task_table ORDER BY taskId DESC LIMIT 1")
    suspend fun getNewTask(): Task

    /** Delete a task from the table */
    @Query("DELETE FROM task_table WHERE taskId = :key")
    suspend fun deleteTask(key: Long)

    /** Delete all tasks from the table */
    @Query("DELETE FROM task_table")
    suspend fun clear()

    /** Select all tasks from the table */
    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>

//    /**
//     * Gets the task's timer
//     * */
//    @Transaction
//    @Query("SELECT * FROM task_table")
//    fun getTaskAndTimer(): List<TaskAndTimer>
//
//    /** Insert a new timer into the table */
//    @Insert
//    suspend fun insertTimer(timer: Timer)
//
//    /** Update the timer */
//    @Update
//    suspend fun updateTimer(timer: Timer)
//
//    /** Select the timer with the corresponding ids */
//    @Query("SELECT * FROM timer_table WHERE timerId = :timerId")
//    suspend fun getTimer(timerId: Long): Timer
//
//    /** Delete timer from table */
//    @Query("DELETE FROM timer_table WHERE timerId = :key")
//    suspend fun deleteTimer(key: Long)
//
//    /** Delete all timers from the table */
//    @Query("DELETE FROM timer_table")
//    suspend fun clearTimerTable()
}