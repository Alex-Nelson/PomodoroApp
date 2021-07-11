package com.example.pomodoroapp.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoroapp.data.TaskDao

/**
 * The [ViewModel] for the TaskListFragment. It loads in the list of tasks for the [TaskAdapter]
 * to bind in the UI. It also has the state variables for navigating either to edit/create a task
 * or the task itself.
 * */
class TaskListViewModel(dataSource: TaskDao): ViewModel() {

    // Hold a reference to the TaskDatabase via TaskDao
    private val database = dataSource

    // List of task names
    var taskList = database.getAllTasks()

    /** State variables */
    private val _navigateToManageTask = MutableLiveData<Boolean>()
    val navigateToManageTask: LiveData<Boolean> get() = _navigateToManageTask

    /**
     * Let the TaskListFragment know to navigate to ManageTaskFragment when the FAB is clicked
     * */
    fun onFabClicked() {
        _navigateToManageTask.value = true
    }

    /**
     * Call this immediately after navigating to ManageTaskFragment
     * */
    fun doneNavigatingToManageTask() {
        _navigateToManageTask.value = false
    }

}