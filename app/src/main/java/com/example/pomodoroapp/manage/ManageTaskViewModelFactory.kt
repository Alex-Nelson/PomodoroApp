package com.example.pomodoroapp.manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoroapp.data.TaskDao

/**
 * ViewModelFactory for the [ManageTaskViewModel]. It passes the id and DAO to the viewModel.
 * */
class ManageTaskViewModelFactory(
        private val id: Long, private val dataSource: TaskDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ManageTaskViewModel::class.java)){
            return ManageTaskViewModel(id, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}