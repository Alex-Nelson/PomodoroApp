package com.example.pomodoroapp.manage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoroapp.data.Task
import com.example.pomodoroapp.data.TaskDao
import kotlinx.coroutines.launch

/**
 * The [ViewModel] for the [ManageTaskFragment]. It handles all the data changes the user makes in
 * the fragments UI through the TextInputLayouts (for all the task attributes) or by using the
 * ImageButtons for the Int values.
 * */
class ManageTaskViewModel(key: Long, private val database: TaskDao) : ViewModel() {

    // Values of the task
    private var taskKey = key

    private var _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private var _sessionTime = MutableLiveData<Int?>()
    val sessionTime: LiveData<Int?> get() = _sessionTime

    private var _numSessions = MutableLiveData<Int?>()
    val numSessions: LiveData<Int?> get() = _numSessions

    private var _shortTime = MutableLiveData<Int?>()
    val shortTime: LiveData<Int?> get() = _shortTime

    private var _longTime = MutableLiveData<Int?>()
    val longTime: LiveData<Int?> get() = _longTime

    private var currentTask = MutableLiveData<Task?>()

    // State variables
    private var _navigateToTask = MutableLiveData<Long?>()
    val navigateToTask: LiveData<Long?> get() = _navigateToTask

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean> get() = _showSnackbarEvent

    init {

        // If the key is 0, create a new task
        if(taskKey == 0L){
            createTask()
        }else {
            // Get the task that needs to be edited.
            retrieveTask(taskKey)
        }
    }

    /**
     * Create a new task
     * */
    private fun createTask(){
        _name.value = ""
        _sessionTime.value = 25
        _numSessions.value = 4
        _shortTime.value = 5
        _longTime.value = 15
    }

    /**
     * Get the task from the database
     * */
    private fun retrieveTask(id: Long?){

        viewModelScope.launch {
            currentTask.value = id?.let { getTaskFromDatabase(it) }

            // Store the values from the database into [currentTask]
            _name.value = currentTask.value?.taskName
            _sessionTime.value = currentTask.value?.sessionLength
            _numSessions.value = currentTask.value?.numberOfSessions
            _shortTime.value = currentTask.value?.shortBreakLength
            _longTime.value = currentTask.value?.longBreakLength
        }

    }

    private suspend fun getTaskFromDatabase(id: Long): Task{
        return database.getTask(id)
    }

    /**
     * Update the task
     * */
    private fun updateTask(){
        // Update the values in the current task
        currentTask.value?.taskName = name.value.toString()
        currentTask.value?.sessionLength = sessionTime.value!!
        currentTask.value?.numberOfSessions = numSessions.value!!
        currentTask.value?.shortBreakLength = shortTime.value!!
        currentTask.value?.longBreakLength = longTime.value!!

        // Update the database
        viewModelScope.launch {
            currentTask.value?.let { database.update(it) }
        }
    }

    /**
     * Insert a new task into the database using the user's input
     * */
    private suspend fun insertNewTask(){
        val newTask = Task(0L, name.value!!, sessionTime.value!!, shortTime.value!!,
                longTime.value!!, numSessions.value!!)

        database.insert(newTask)

        // Get the newest task's id
        taskKey = database.getNewTask().taskId

    }

    /**
     * Updates the task with any changes the user made and navigates to TaskFragment
     * */
    fun onSubmit() {
        viewModelScope.launch {
            // If the taskKey is 0, inset it
            if(taskKey == 0L){
                insertNewTask()
            } else {
                // Update the task
                updateTask()
            }

            // Set this state variable to true to alert the observer and trigger navigation
            _showSnackbarEvent.value = true

            // Set this state variable to the task's value to alert the observer
            _navigateToTask.value = taskKey
        }
    }

    /**
     * Called immediately after calling 'show()' on a toast
     * */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    /**
     * Called immediately after navigating to TaskFragment
     * */
    fun doneNavigating(){
        _navigateToTask.value = null
    }

    /**
     * UI functions
     * */
    /** Increment the length of the session */
    fun incSessionTime(){
        if(sessionTime.value!! < 60) {
            _sessionTime.value = sessionTime.value?.plus(1)
        }
    }

    /** Decrement the length of the session */
    fun decSessionTime(){
        if(sessionTime.value!! > 1){
            _sessionTime.value = sessionTime.value?.minus(1)
        }
    }

    /** Increment the number of sessions */
    fun incNumSessions(){
        if(numSessions.value!! < 10){
            _numSessions.value = numSessions.value?.plus(1)
        }
    }

    /** Decrement the number of sessions */
    fun decNumSessions(){
        if(numSessions.value == null){
            // TODO: //TODO: Add a flag to show error text
        }
        else if(numSessions.value!! > 1){
            _numSessions.value = numSessions.value?.minus(1)
        }
    }

    /** Increment the length of the short break */
    fun incShortBreakTime(){
        if(shortTime.value == null){
            //TODO: Add a flag to show error text
        }
        else if(shortTime.value!! < 40){
            _shortTime.value = shortTime.value?.plus(1)
        }
    }

    /** Decrement the length of the short break */
    fun decShortBreakTime(){
        if(shortTime.value!! > 0){
            _shortTime.value = shortTime.value?.minus(1)
        }
    }

    /** Increment the length of the long break */
    fun incLongBreakTime(){
        if(longTime.value!! < 50){
            _longTime.value = longTime.value?.plus(1)
        }
    }

    /** Decrement the length of the long break */
    fun decLongBreakTime(){
        if(longTime.value!! > 1){
            _longTime.value = longTime.value?.minus(1)
        }
    }

    /**
     * Functions that check if the user edited the values in the EditText fields
     * */
    fun setName(newName: String){
        _name.value = newName
    }

    fun setSessionLength(newTime: String){
        when (newTime) {
            "" -> _sessionTime.value = null
            "null" -> _sessionTime.value = 0
            else -> _sessionTime.value = Integer.parseInt(newTime)
        }
    }

    fun setShortBreakLength(newTime: String){
        when(newTime){
            "" -> _shortTime.value = null
            "null" -> _shortTime.value = 0
            else -> _sessionTime.value = Integer.parseInt(newTime)
        }
    }

    fun setLongBreakLength(newTime: String){
        when(newTime){
            "" -> _longTime.value = null
            "null" -> _longTime.value = 0
            else -> _longTime.value = Integer.parseInt(newTime)
        }
    }

    fun setNumSessions(newNumber: String){
        when(newNumber){
            "" -> _numSessions.value = null
            "null" -> _numSessions.value = 0
            else -> _numSessions.value = Integer.parseInt(newNumber)
        }
    }
}