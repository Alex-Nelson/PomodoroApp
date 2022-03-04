package com.example.pomodorotimerapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimerapplication.data.AppRepository
import com.example.pomodorotimerapplication.data.Task

class RunTimerViewModelFactory(
    //private val task: Task,
    private val repository: AppRepository
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RunTimerViewModel::class.java)) {
            //return RunTimerViewModel(task, repository) as T
            return RunTimerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}