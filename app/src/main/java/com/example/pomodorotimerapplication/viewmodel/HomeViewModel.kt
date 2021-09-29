package com.example.pomodorotimerapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.pomodorotimerapplication.data.AppDatabase
import com.example.pomodorotimerapplication.data.AppRepository
import com.example.pomodorotimerapplication.data.Task

/**
 * The [AndroidViewModel] for the Home Screen.
 * */
class HomeViewModel(application: Application): AndroidViewModel(application) {

    val repository: AppRepository
    val allTasksList: LiveData<List<Task>>

    init {
        val appDB = AppDatabase.getInstance(application).appDao
        repository = AppRepository(appDao = appDB)
        allTasksList = repository.getAllTasks()
    }

}