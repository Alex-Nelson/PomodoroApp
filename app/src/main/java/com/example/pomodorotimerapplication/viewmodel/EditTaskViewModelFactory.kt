package com.example.pomodorotimerapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodorotimerapplication.data.AppRepository

class EditTaskViewModelFactory(
    private val key: Long,
    private val repository: AppRepository
) : ViewModelProvider.Factory
{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(EditTaskViewModel::class.java)){
            return EditTaskViewModel(key, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}