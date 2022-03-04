package com.example.pomodorotimerapplication.ui.runtimer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodorotimerapplication.data.Task
import com.example.pomodorotimerapplication.utilities.TimerUtils
import com.example.pomodorotimerapplication.viewmodel.RunTimerViewModel

@Composable
fun RunTimerBody(
    viewModel: RunTimerViewModel,
    task: Task,
    onReturnHome: () -> Boolean
) {

    // Retrieve the pomodoro timer that was clicked on/created
    viewModel::setTimer.invoke(task)

    // Show an alert dialog to confirm user wants to delete timer
    val showDeleteDialog = remember { mutableStateOf(false) }
    if(showDeleteDialog.value) {
        //TODO: Call delete alert dialog
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = task.taskName) },
                navigationIcon = {
                    IconButton(onClick = {
                        // TODO: reset the values in view model
                        onReturnHome.invoke()
                    } )
                    {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Navigate back to Home Screen"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO: Display a drop down menu*/ } ) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {

    }
}

///**
// *
// * @param task
// * @param timerStatus  the current status of the timer
// * @param timerType  the type of timer being run
// * @param timerString  the string version of the current timer value
// * @param onStartPause  notify caller to start or pause the timer
// * @param onReset  notify caller to reset the state of the screen to default values
// * @param onAutoStart  notify caller to start timer automatically
// * */
//@Composable
//fun RunTimerScreen(
//    task: Task,
//    timerStatus: TimerUtils.TimerState,
//    timerType: TimerUtils.TimerType,
//    timerString: String,
//    onStartPause: () -> Unit,
//    onReset: () -> Unit,
//    onAutoStart: () -> Unit
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = task.taskName) },
//                navigationIcon = {
//                    IconButton(onClick = { /*TODO: Navigate back to home screen*/ } ) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Navigate back to Home Screen"
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { /*TODO: Display a drop down menu*/ } ) {
//                        Icon(
//                            Icons.Filled.MoreVert,
//                            contentDescription = null
//                        )
//                    }
//                }
//            )
//        }
//    ) {
//        Column(modifier = Modifier.padding(horizontal = 105.dp, vertical = 40.dp)) {
//            Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
//                // Display the current timer
//                TimerDisplay(
//                    timerType = timerType,
//                    timerString = timerString
//                )
//            }
//            Row {
//                // Display the buttons for the timer
//                IconButtonRow(
//                    timerStatus = timerStatus,
//                    onClick = onStartPause,
//                    onReset = onReset
//                )
//            }
//            /* TODO: Add card to allow user to add smaller tasks to do */
//        }
//    }
//}
//
///**
// * A stateful composable to display the timer's current status
// *
// * @param timerType the type of timer being run
// * @param timerString a string representation of the string
// * */
//@Composable
//fun TimerDisplay(
//    timerType: TimerUtils.TimerType,
//    timerString: String
//) {
//
//    val displayText = when (timerType) {
//        TimerUtils.TimerType.Session -> "Session"
//        TimerUtils.TimerType.ShortBreak -> "Short Break"
//        TimerUtils.TimerType.LongBreak -> "Long Break"
//        else -> "Finished"
//    }
//
//    Column(horizontalAlignment = Alignment.CenterHorizontally)
//    {
//        Text(
//            text = displayText,
//            fontSize = 42.sp
//        )
//        Text(
//            text = timerString,
//            fontSize = 66.sp
//        )
//    }
//}
//
///**
// *
// * @param timerStatus the current state of the timer
// * @param onClick notify caller to play/pause the timer
// * @param onReset notify caller to reset UI
// * */
//@Composable
//fun IconButtonRow(
//    timerStatus: TimerUtils.TimerState,
//    onClick: () -> Unit,
//    onReset: () -> Unit
//) {
//    // Switch which icon is displayed for running/paused
//    val isRunning by remember {
//        mutableStateOf(timerStatus)
//    }
//    val icon = if(isRunning != TimerUtils.TimerState.Running) Icons.Default.PlayArrow
//               else Icons.Default.Pause
//
//    Column {
//        Row(modifier = Modifier
//            .padding(top = 40.dp)
//            .align(Alignment.End)
//        ) {
//            // Icon button to play/pause
//            IconButton(
//                onClick = onClick
//            ) {
//                Icon(
//                    imageVector = icon,
//                    contentDescription = "Runs/Pauses the Pomodoro timer.",
//                    modifier = Modifier
//                        .padding(start = 20.dp, end = 20.dp)
//                        .size(60.dp)
//                )
//            }
//
//            // Icon button to reset
//            IconButton(
//                onClick = onReset
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Replay,
//                    contentDescription = "Resets the Pomodoro timer.",
//                    modifier = Modifier
//                        .padding(top = 5.dp)
//                        .size(50.dp)
//                )
//            }
//
//            /* TODO: Add auto start check box */
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewRunTimerScreen() {
//    val task = Task(1, "Study for Physics III test", 25,
//        5, 15, 4, "In Progress")
//    RunTimerScreen(
//        task = task,
//        timerStatus = TimerUtils.TimerState.Paused,
//        timerType = TimerUtils.TimerType.Session,
//        timerString = "25:00",
//        onStartPause = { },
//        onReset = { },
//        onAutoStart = { }
//    )
//}
//
//
//@Preview
//@Composable
//fun PreviewTimerDisplay() {
//    TimerDisplay(
//        timerType = TimerUtils.TimerType.ShortBreak,
//        timerString = "05:00"
//    )
//}
//
//@Preview
//@Composable
//fun PreviewIconButtonRow() {
//    IconButtonRow(
//        timerStatus = TimerUtils.TimerState.Paused,
//        onClick = { },
//        onReset = { }
//    )
//}