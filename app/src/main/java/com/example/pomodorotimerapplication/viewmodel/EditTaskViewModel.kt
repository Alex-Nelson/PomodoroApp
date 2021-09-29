package com.example.pomodorotimerapplication.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pomodorotimerapplication.data.AppRepository
import com.example.pomodorotimerapplication.data.Task
import kotlinx.coroutines.runBlocking

class EditTaskViewModel(key: Long, repository: AppRepository): ViewModel() {

    var currentTask = mutableStateOf<Task?>(null)
        private set

    // keep a reference to the repository
    private val repo = repository

    init {
        if(key == 0L){
            // Set current task to a new task with default values
            currentTask.value = Task(key, "", 25,
                5, 15, 4)
        }else{
            // Retrieve the task with the corresponding key
            runBlocking {
                currentTask.value = repo.retrieveTask(key)
            }
        }
    }

    /**
     * Saves a name for the current task
     *
     * @param name a String of the user's input
     * */
    fun onSaveName(name: String){
        if(currentTask.value!!.taskName != name){
            currentTask.value!!.taskName = name
        }
    }

    /**
     *
     * @param num an integer for the length of each
     * */
    fun setSessionLength(num: Int){
        currentTask.value?.sessionLen = num
    }

    /**
     *
     * @param num an integer for the length of each short break
     * */
    fun setShortBreakLen(num: Int){
        currentTask.value?.shortBreakLen = num
    }

    /**
     *
     * @param num an integer for the length of each long break
     * */
    fun setLongBreakLen(num: Int){
        currentTask.value?.longBreakLen = num
    }

    /**
     *
     * @param num an integer for the number of sessions
     * */
    fun setNumberSessions(num: Int){
        currentTask.value?.numOfSessions = num
    }

    /**
     * Save any changes made the task to the database
     * */
    fun saveTask(){

        require(currentTask.value!!.taskName != ""){
            "Please enter a name for the pomodoro timer"
        }

        runBlocking {
            if(currentTask.value!!.taskId == 0L){
                repo.addTask(currentTask.value!!)
            }else{
                repo.updateTask(currentTask.value!!)
            }
        }
    }
}