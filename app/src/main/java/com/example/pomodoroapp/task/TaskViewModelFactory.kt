package com.example.pomodoroapp.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroapp.data.TaskDao

/**
 * The ViewModelFactory for the TaskViewModel. It passes the taskKey and DAO to the ViewModel
 * to use.
 * */
class TaskViewModelFactory(
        private val taskKey: Long, private val dataSource: TaskDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            return TaskViewModel(taskKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}