package com.example.pomodorotimerapplication.data

import androidx.lifecycle.LiveData

/**
 * A repository to allow abstract access to the database
 * */
class AppRepository(private val appDao: AppDao) {

    fun getAllTasks(): LiveData<List<Task>> = appDao.getAllTasks()

    fun getAllTaskItems(key: Long):
            LiveData<List<TaskWithTaskItems>> = appDao.getTaskWithTaskItem(key)

    suspend fun getTaskTimer(key: Long): TaskAndTimer = appDao.getTaskAndTimer(key)

    suspend fun addTask(task: Task) = appDao.insertTask(task)

    suspend fun updateTask(task: Task) = appDao.updateTask(task)

    suspend fun retrieveTask(key: Long): Task = appDao.getTask(key)

    suspend fun retrieveNewTask(): Task = appDao.getNewTask()

    suspend fun removeTask(key: Long) = appDao.deleteTask(key)

    suspend fun removeAllTasks() = appDao.deleteAllTasks()

    suspend fun addTimer(timer: RecentTimer) = appDao.insertTimer(timer)

    suspend fun updateTimer(timer: RecentTimer) = appDao.updateTimer(timer)

    suspend fun removeTimer(timer: RecentTimer) = appDao.deleteTimer(timer)

    suspend fun removeAllTimers() = appDao.deleteAllTimers()

    suspend fun addItem(taskItem: TaskItem) = appDao.insertItem(taskItem)

    suspend fun updateItem(taskItem: TaskItem) = appDao.updateItem(taskItem)

    suspend fun removeItem(taskItem: TaskItem) = appDao.deleteItem(taskItem)

    suspend fun removeItems(key: Long) = appDao.deleteItems(key)

    suspend fun removeAllItems() = appDao.deleteAllItems()
}