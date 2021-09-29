package com.example.pomodorotimerapplication.data

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * A [Dao] for the tables in the app's room database.
 * */
@Dao
interface AppDao {

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("SELECT * FROM task_table WHERE id = :key")
    suspend fun getTask(key: Long): Task

    @Query("SELECT * FROM task_table ORDER BY id DESC LIMIT 1")
    suspend fun getNewTask(): Task

    @Query("DELETE FROM task_table WHERE id = :key")
    suspend fun deleteTask(key: Long)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM task_table")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimer(timer: RecentTimer)

    @Update
    suspend fun updateTimer(timer: RecentTimer)

    @Delete
    suspend fun deleteTimer(timer: RecentTimer)

    @Query("DELETE FROM timer_table")
    suspend fun deleteAllTimers()

    @Transaction
    @Query("SELECT * FROM task_table WHERE id = :key")
    suspend fun getTaskAndTimer(key: Long): TaskAndTimer

    @Insert
    suspend fun insertItem(taskItem: TaskItem)

    @Update
    suspend fun updateItem(taskItem: TaskItem)

    @Delete
    suspend fun deleteItem(taskItem: TaskItem)

    @Query("DELETE FROM task_item_table WHERE taskId = :key")
    suspend fun deleteItems(key: Long)

    @Query("DELETE FROM task_item_table")
    suspend fun deleteAllItems()

    @Transaction
    @Query("SELECT * FROM task_table WHERE id = :key")
    fun getTaskWithTaskItem(key: Long): LiveData<List<TaskWithTaskItems>>
}