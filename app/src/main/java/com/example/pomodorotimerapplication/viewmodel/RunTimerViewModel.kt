package com.example.pomodorotimerapplication.viewmodel

import android.app.PendingIntent
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pomodorotimerapplication.data.AppRepository
import com.example.pomodorotimerapplication.data.Task
import com.example.pomodorotimerapplication.data.TaskItem
import com.example.pomodorotimerapplication.utilities.TimerUtils
import com.example.pomodorotimerapplication.utilities.TimerUtils.Companion.DONE
import com.example.pomodorotimerapplication.utilities.TimerUtils.Companion.ONE_SECOND
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

/**
 * The [ViewModel] for the Run Timer Screen.
 *
 * @param task the pomodoro timer that will be used on the screen
 * @param repository the app's repository
 * */
//class RunTimerViewModel(task: Task, repository: AppRepository) : ViewModel() {
class RunTimerViewModel(
    repository: AppRepository
) : ViewModel() {
    // The current pomodoro timer
    private var _currentTask: Task? by mutableStateOf(null)
    val currentTask: Task?
        get() = _currentTask

    private val repo = repository

    // Timer
    private lateinit var timer: CountDownTimer

    // State for the number of sessions
    var counter = mutableStateOf(1)
        private set

    // The current timer's value
    private var _currentTimer = MutableLiveData<Long>()
    private val currentTimer: LiveData<Long> get() = _currentTimer

    // State for enabling/disabling auto starting a timer
    var autoStart = mutableStateOf(false)
        private set

    // The saved time value when the timer is paused
    private var savedTime = DONE

    private var _itemList = MutableLiveData<List<TaskItem>>()
    val itemList: LiveData<List<TaskItem>> = _itemList

    // The status of the timer
    var timerStatus = mutableStateOf(TimerUtils.TimerState.Stopped)
        private set

    // The current timer type
    var timerType = mutableStateOf(TimerUtils.TimerType.Session)
        private set

    val currentTimeString = Transformations.map(currentTimer) { time ->
        DateUtils.formatElapsedTime(time)
    }

    /**
     * Set the current pomodoro timer
     *
     * @param task a [Task] that represents a Pomodoro timer the user created
     * */
    fun setTimer(task: Task) {
        viewModelScope.launch {
            _currentTask = repo.retrieveTask(task.taskId)
        }
    }

    /**
     * Add a item to the list of items for the task
     * */
    fun onAddItem(item: TaskItem) {
        viewModelScope.launch {
            repo.addItem(item)

            // TODO: retrieve task item list
            //_itemList.value = repo.getAllTaskItems(currentTask.taskId)
        }
    }

    /**
     * Toggle auto starting a timer when it reaches 00:00
     * */
    fun onAutoStart() {
        autoStart.value = autoStart.value == false
    }

    /**
     * Delete the timer.
     * */
    fun onDeleteTimer(){
        viewModelScope.launch {
            // TODO: delete any items first

            repo.removeTask(currentTask!!.taskId)
        }
    }

    /**
     * Calls onPause if timer is running or onStart if timer is not running
     * */
    fun onStartPause() {
        if(timerStatus.value == TimerUtils.TimerState.Running) {
            onPause()
        }else {
            onStart()
        }
    }

    /**
     * Stop the timer if running and save current time value.
     * */
    private fun onPause() {
        if(timerStatus.value == TimerUtils.TimerState.Running) {
            timerStatus.value = TimerUtils.TimerState.Paused

            // Save current time value
            savedTime = currentTimer.value!!.times(ONE_SECOND)

            // cancel timer
            timer.cancel()
        }
    }

    /**
     * Start the timer and have it count down to 00:00 or is paused
     * */
    private fun onStart() {
        // If timer is not currently running, start the timer
        if(timerStatus.value != TimerUtils.TimerState.Running) {
            if(savedTime != DONE) {
                // Used the saved time value if one was saved
                setUpTimer(savedTime)
            }

            // start the timer
            timerStatus.value = TimerUtils.TimerState.Running
            timer.start()
        }
    }

    /**
     * Reset all values.
     * */
    fun onReset() {
        timerStatus.value = TimerUtils.TimerState.Stopped
        timerType.value = TimerUtils.TimerType.Session
        counter.value = 1
        autoStart.value = false
        _currentTimer.value = currentTask!!.sessionLen.toLong()
        savedTime = DONE
    }

    /**
     * Determines which notification to send to the user
     * */
    private fun setNotification() {
//        if((counter.value == 1 && currentTask.taskStatus == "Session" &&
//                    timerStatus.value == TimerUtils.TimerState.Stopped) ||
//            (currentTask.taskStatus == "Session" && ))
    }

    /**
     * Set up the timer
     *
     * @param timeValue
     * */
    private fun setUpTimer(timeValue: Long) {
        timer = object : CountDownTimer(timeValue, ONE_SECOND) {

            /**
             * Decrement the timer's value by one second
             *
             * @param millisUntilFinished
             * */
            override fun onTick(millisUntilFinished: Long) {
                _currentTimer.value = millisUntilFinished / ONE_SECOND
            }

            /**
             * Set up the next timer once the current timer reaches zero
             * */
            override fun onFinish() {
                timerStatus.value = TimerUtils.TimerState.Stopped
                _currentTimer.value = DONE
                savedTime = DONE

                // TODO: May move this to separate function
                if(timerType.value == TimerUtils.TimerType.Session) {
                    // Set up a break timer
                    setUpBreakTimer()
                } else {
                    // Set up a session timer
                    setUpSessionTimer()
                }
            }

        }
    }

    /**
     * Increment/reset the counter and set the timer value to the session length
     * */
    private fun setUpSessionTimer() {
        if(timerType.value == TimerUtils.TimerType.LongBreak) {
            // Reset the counter back to 1
            counter.value = 1
            timerType.value = TimerUtils.TimerType.Finished
        }else {
            // Increment counter
            counter.value++

            // Set up the session timer
            timerType.value = TimerUtils.TimerType.Session
            setUpTimer(currentTask!!.sessionLen.toLong())
            _currentTimer.value = currentTask!!.sessionLen.toLong().div(ONE_SECOND)
        }
    }

    /**
     * Set up the timer for a break
     * */
    private fun setUpBreakTimer() {
        if(counter.value == currentTask!!.numOfSessions) {
            // Start a long break timer
            timerType.value = TimerUtils.TimerType.LongBreak
            setUpTimer(currentTask!!.longBreakLen.toLong())
            _currentTimer.value = currentTask!!.longBreakLen.toLong().div(ONE_SECOND)
        } else {
            // Start a short break timer
            timerType.value = TimerUtils.TimerType.ShortBreak
            setUpTimer(currentTask!!.shortBreakLen.toLong())
            _currentTimer.value = currentTask!!.shortBreakLen.toLong().div(ONE_SECOND)
        }
    }
}