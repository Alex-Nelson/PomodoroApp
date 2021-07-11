package com.example.pomodoroapp.task

import android.content.Context
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*
import com.example.pomodoroapp.data.Task
import com.example.pomodoroapp.data.TaskDao
import com.example.pomodoroapp.util.Notifications
import com.example.pomodoroapp.util.PrefUtil
import com.example.pomodoroapp.util.TimerNotifications
import com.example.pomodoroapp.util.TimerUtil
import kotlinx.coroutines.launch

/**
 * [ViewModel] for the [TaskFragment].
 *
 * Handles the timers for the sessions and breaks. It also retrieves the task's values from the
 * database so the UI can update.
 * */
class TaskViewModel(private val key: Long, dataSource: TaskDao) : ViewModel() {

    companion object {
        // Time when the timer is done
        private const val DONE = 0L

        // Countdown time interval
        private const val ONE_SECOND = 1000L
    }

    // Holds a reference to the TaskDatabase via its TaskDao
    private val database = dataSource

    // Timer
    private lateinit var timer: CountDownTimer

    // The saved time value when the timer is paused
    private var savedTime = DONE

    // String to keep track of what type of timer is running
    private var _timerType = MutableLiveData<String>()
    val timerType: LiveData<String> get() = _timerType

    // The current time value
    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long> get() = _currentTime

    // The String version of the current time
    val currentTimeString = Transformations.map(currentTime) { time ->
        DateUtils.formatElapsedTime(time)
    }

    // Keeps track of the timer's status
    private var _timerStatus = MutableLiveData<TimerUtil.TimerState>()
    val timerStatus: LiveData<TimerUtil.TimerState> get() = _timerStatus

    private var _notifyState = MutableLiveData<String>()
    val notifyState: LiveData<String> get() = _notifyState

    // Task values
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _sessionTime = MutableLiveData<Long>()
    private val sessionTime: LiveData<Long> get() = _sessionTime

    private val _numSessions = MutableLiveData<Int>()
    val numSessions: LiveData<Int> get() = _numSessions

    private val _shortTime = MutableLiveData<Long>()
    private val shortTime: LiveData<Long> get() = _shortTime

    private val _longTime = MutableLiveData<Long>()
    private val longTime: LiveData<Long> get() = _longTime

    private var task = MutableLiveData<Task>()

    // The number of sessions that have taken place
    private var _counter = MutableLiveData<Int>()
    val counter: LiveData<Int> get() = _counter

    // State variable to navigate to ManageTaskFragment
    private val _navigateToManageTask = MutableLiveData<Long?>()
    val navigateToManageTask: LiveData<Long?> get() = _navigateToManageTask

    // Let the TaskFragment know to navigate back to TaskListFragment
    private val _navigateToTaskList = MutableLiveData<Boolean>()
    val navigateToTaskList: LiveData<Boolean> get() = _navigateToTaskList

    // State variable for showing snackbar
    private val _showSnackbarDelete = MutableLiveData<Boolean>()
    val showSnackbarDelete: LiveData<Boolean> get() = _showSnackbarDelete

    // State variable for enabling/disabling auto start
    private val _autoStart = MutableLiveData<Boolean>()
    val autoStart: LiveData<Boolean> get() = _autoStart

    init {
        _timerStatus.value = TimerUtil.TimerState.Stopped
        _timerType.value = "Session"
        _counter.value = 1
        _autoStart.value = false
        setNotification()

        initializeTask(key)
    }

    /**
     * Gets the task from the database, stores the values, and calls setUpTimer to set up the first
     * timer
     * */
    private fun initializeTask(key: Long){
        viewModelScope.launch {
            task.value = getTask(key)

            _name.value = task.value?.taskName
            _numSessions.value = task.value?.numberOfSessions

            // Convert the time values from minutes to milliseconds for the timer to be accurate
            _sessionTime.value = task.value?.sessionLength?.toLong()?.times(60000L)
            _longTime.value = task.value?.longBreakLength?.toLong()?.times(60000L)
            _shortTime.value = task.value?.shortBreakLength?.toLong()?.times(60000L)
            _currentTime.value = _sessionTime.value?.div(ONE_SECOND)

            // Set up the initial timer
            setUpTimer(sessionTime.value!!)
        }
    }

    /**
     * Get the specified task from the database to get its values
     * */
    private suspend fun getTask(key: Long): Task {
        return database.getTask(key)
    }

    /**
     * Sets up the timer.
     * */
    private fun setUpTimer(timeValue: Long){
        timer = object : CountDownTimer(timeValue, ONE_SECOND) {

            // Decrement the timer's value by one second
            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = millisUntilFinished / ONE_SECOND
            }

            // When the timer reaches zero, decide which timer it will start next
            override fun onFinish() {
                _timerStatus.value = TimerUtil.TimerState.Stopped
                _currentTime.value = DONE
                setUpNextTimer()
            }
        }
    }

    /**
     * Decides which timer needs to be set up based on the previous timer.
     * */
    private fun setUpNextTimer(){

        // Reset the savedTime value before going to the next timer
        savedTime = DONE

        if(timerType.value?.equals("Session") == true) {
            setUpBreakTimer()
        } else {
            setUpSessionTimer()
        }

        // If autoStart is true, start the timer automatically
        if (autoStart.value == true) {
            timer.start()
            _timerStatus.value = TimerUtil.TimerState.Running
        }

        //setNotification()

    }

    /**
     * Increments the counter and sets the timer value to the session length
     * */
    private fun setUpSessionTimer() {
        if(timerType.value?.equals("Long Break") == true) {
            _counter.value = 1
        }else {
            _counter.value = _counter.value?.plus(1)
        }

        _timerType.value = "Session"
        setUpTimer(sessionTime.value!!)
        _currentTime.value = _sessionTime.value?.div(ONE_SECOND)

    }

    /**
     * Sets the timer to a break length
     * */
    private fun setUpBreakTimer(){
        if(counter.value == numSessions.value){
            _timerType.value = "Long Break"
            setUpTimer(longTime.value!!)
            _currentTime.value = _longTime.value?.div(ONE_SECOND)
        } else {
            _timerType.value = "Short Break"
            setUpTimer(shortTime.value!!)
            _currentTime.value = _shortTime.value?.div(ONE_SECOND)
        }

    }

    /**
     * Executes when the RESET button is pressed.
     *
     * All the UI values are reset to the default values.
     * */
    fun onReset(){
        timer.cancel()
        _timerStatus.value = TimerUtil.TimerState.Stopped
        _timerType.value = "Session"
        setUpTimer(sessionTime.value!!)

        savedTime = DONE
        _currentTime.value = _sessionTime.value?.div(ONE_SECOND)
        _autoStart.value = false

        _counter.value = 1
        //setNotification()
    }

    /**
     * Executes when the START button is pressed
     *
     * The timer will start to count down until it reaches 0:00
     * */
    fun onStart() {
        // If the timer is not running, start the timer
        if(timerStatus.value == TimerUtil.TimerState.Paused ||
                    timerStatus.value == TimerUtil.TimerState.Stopped) {
            // If there is a saved time value, set the currentTime value to it
            if(savedTime != DONE) {
                setUpTimer(savedTime)
            }

            // Start the timer
            _timerStatus.value = TimerUtil.TimerState.Running
            timer.start()
        }
        //setNotification()
    }

    /**
     * Executes when the PAUSE button is pressed.
     *
     * It will stop the timer and store the current time so it can resume from that time.
     * */
    fun onPause() {
        // If the timer is running, pause the timer
        if(timerStatus.value == TimerUtil.TimerState.Running) {
            _timerStatus.value = TimerUtil.TimerState.Paused

            // Save the current time value
            savedTime = currentTime.value!!.times(ONE_SECOND)

            // Cancel the timer
            timer.cancel()
        }
        //setNotification()
    }

    /**
     * Executes when the Switch is pressed.
     *
     * Toggles whether the timers will automatically start the next timer when it reaches 0:00.
     * */
    fun onAutoStart() {
        _autoStart.value = autoStart.value == false
    }

    /**
     * Called immediately after navigating to ManageTaskFragment
     * */
    fun doneNavigatingToManageTask() {
        _navigateToManageTask.value = null
    }

    /**
     * Delete the task and go back to the TaskFragment List
     * */
    fun deleteTask(){
        viewModelScope.launch {
            database.deleteTask(key)

            // Set the state variables
            _navigateToTaskList.value = true
            _showSnackbarDelete.value = true
        }
    }

    /**
     * Called immediately after snackbar is called
     * */
    fun doneShowingSnackbarDelete(){
        _showSnackbarDelete.value = false
    }

    /**
     * Called immediately after navigating to TaskListFragment
     * */
    fun doneNavigatingToTaskList(){
        _navigateToTaskList.value = false
    }

    /**
     * Sets up the notifications when the fragment loses focus
     * */
    fun setUpNotifications(context: Context){
        // If the timer is not running
        when(notifyState.value) {
            "Start Session" -> {
                setWorkNotification(context)
            }
            "Start Break" -> {
                setBreakNotification(context)
            }
            "Finish Pomodoro" -> {
                setFinishPomodoroNotification(context)
            }else -> {
                setTimerNotifications(context)
            }
        }
    }

    /**
     * Sets a notification that notifies the user to start working again
     * */
    private fun setWorkNotification(context: Context){
        Notifications.createChannel(context, "SESSION_EVENT_CHANNEL_ID",
            "Session Events", true)

        Notifications.notifyUser(context, "Start Session",
            "SESSION_EVENT_CHANNEL_ID")
    }

    /**
     * Sets a notification that notifies the user that the pomodoro is finished
     * */
    private fun setFinishPomodoroNotification(context: Context){
        Notifications.createChannel(context, "FINISH_EVENT_CHANNEL_ID",
            "Finish Events", true)

        Notifications.notifyUser(context, "Finish Pomodoro",
            "FINISH_EVENT_CHANNEL_ID")
    }

    /**
     * Sets a notification that notifies the user to start their break
     * */
    private fun setBreakNotification(context: Context){
        Notifications.createChannel(context, "BREAK_EVENT_CHANNEL_ID",
            "Break Events", true)

        Notifications.notifyUser(context, "Start Break",
            "BREAK_EVENT_CHANNEL_ID")
    }

    /**
     * Set up the timer notification to run in the background
     * */
    private fun setTimerNotifications(context: Context){

        val timerLength = when (timerType.value){
            "Session" -> sessionTime.value
            "Short Break" -> shortTime.value
            else -> longTime.value
        }
        // If the timer is running, cancel it and send the remaining time to the notification
        if(timerStatus.value == TimerUtil.TimerState.Running){

            timer.cancel()
            val timerDone = TimerUtil.setAlarm(context, timerLength!!, currentTime.value!!)
            //Show timer running notification
            TimerNotifications.showTimerRunning(context, timerDone)
        }else if(timerStatus.value == TimerUtil.TimerState.Paused){
            //Show timer is paused
            TimerNotifications.showTimerPaused(context)
        }

        // Save the timer
        PrefUtil.setPreviousTimerLength(timerLength!!, context)
        PrefUtil.setSecondsRemaining(currentTime.value!!, context)
        PrefUtil.setTimerState(timerStatus.value!!, context)
    }

    /**
     * Resume the timer in the fragment
     * */
    fun resumeTimer(context: Context){
        // Get the timer's status
        _timerStatus.value = PrefUtil.getTimerState(context)

        /**
         * TODO: Set the timer to the new timer value
         * */
        _currentTime.value = PrefUtil.getTimerLength(context, timerType.value!!).times( 60L)
    }

    /**
     * Determines which notification to send
     * */
    private fun setNotification(){
        if((counter.value == 1 && timerType.value == "Session" &&
                    timerStatus.value == TimerUtil.TimerState.Stopped) ||
            (timerType.value == "Session" && currentTimeString.value == "00:00")){
            // Notify user to start working
            _notifyState.value = "Start Session"
        }else if(timerType.value!!.contains("Break") &&
            timerStatus.value == TimerUtil.TimerState.Stopped){
            // Notify user to start a break
            _notifyState.value = "Start Break"
        }else if(counter.value == numSessions.value &&
            timerStatus.value == TimerUtil.TimerState.Stopped){
            // Notify user that the pomodoro is finished
            _notifyState.value = "Finish Pomodoro"
        }else{
            // Show the current time
            _notifyState.value = "Timer Running"
        }
    }

    /**
     * Remove notifications when fragment regains focus
     * */
    fun removeNotifications(context: Context){
        // Get the timer's current status
        val currentStatus = PrefUtil.getTimerState(context)

        if(currentStatus == TimerUtil.TimerState.Running){

        }
    }
}