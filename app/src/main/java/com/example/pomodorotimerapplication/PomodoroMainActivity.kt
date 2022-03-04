package com.example.pomodorotimerapplication

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pomodorotimerapplication.data.Task
import com.example.pomodorotimerapplication.ui.dialog.EditTaskDialog
import com.example.pomodorotimerapplication.ui.home.HomeBody
//import com.example.pomodorotimerapplication.ui.runtimer.RunTimerScreen
import com.example.pomodorotimerapplication.ui.theme.PomodoroTimerApplicationTheme
import com.example.pomodorotimerapplication.utilities.PomodoroScreen
import com.example.pomodorotimerapplication.viewmodel.*
import com.google.gson.Gson

class PomodoroMainActivity : ComponentActivity() {

    @ExperimentalComposeUiApi
    @ExperimentalMaterialApi
    @OptIn(ExperimentalAnimationApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val application = requireNotNull(this).application

        // Set up view model for home screen
        val homeViewModelFactory = HomeViewModelFactory(application)
        val homeViewModel = ViewModelProvider(
            this, homeViewModelFactory)[HomeViewModel::class.java]

        // Set up view model for Edit Task Full Dialog
        val editTaskViewModelFactory = EditTaskViewModelFactory(0L, homeViewModel.repository)
        val editTaskViewModel = ViewModelProvider(
            owner = this, editTaskViewModelFactory)[EditTaskViewModel::class.java]

        // Set up view model for Run Timer Screen
        val runTimerViewModelFactory = RunTimerViewModelFactory(
//            task = Task(taskId = 0L, taskName = "", sessionLen = 25,
//                shortBreakLen = 5, longBreakLen = 15, numOfSessions = 4),
            homeViewModel.repository)
        val runTimerViewModel = ViewModelProvider(
            owner = this, runTimerViewModelFactory)[RunTimerViewModel::class.java]

        setContent {
            PomodoroApp(
                homeViewModel = homeViewModel,
                editTaskViewModel = editTaskViewModel,
                runTimerViewModel = runTimerViewModel
            )
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PomodoroApp(
    homeViewModel: HomeViewModel,
    editTaskViewModel: EditTaskViewModel,
    runTimerViewModel: RunTimerViewModel
) {
    PomodoroTimerApplicationTheme {
        Surface {
            // TODO: Add loading screen

            val navController = rememberNavController()

            Scaffold { innerPadding ->
                PomodoroAppNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    editTaskViewModel = editTaskViewModel,
                    runTimerViewModel = runTimerViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PomodoroAppNavHost(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    editTaskViewModel: EditTaskViewModel,
    runTimerViewModel: RunTimerViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = PomodoroScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = PomodoroScreen.Home.name) {
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable(
            route = "${PomodoroScreen.EditDialog.name}/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
           backStackEntry.arguments?.getLong("taskId", 0L).let {
               CreateFullDialogScreen(
                   navController = navController,
                   viewModel = editTaskViewModel
               )
           }
        }

        composable(
            route = "${PomodoroScreen.RunTimer.name}/{task}",
            arguments = listOf(
                navArgument("task") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("task")?.let { json ->
                val task = Gson().fromJson(json, Task::class.java)
                TimerScreen(
                    navController = navController,
                    runTimerViewModel = runTimerViewModel,
                    task = task
                )
            }
        }
    }
}

/**
 * Screen for the app's home (first thing user sees when app is first launched),
 *
 * @param navController
 * @param homeViewModel the view model used by the screen
 * */
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    // TODO: retrieve all lists from view model
    val allList = homeViewModel.allTasksList.observeAsState(initial = listOf()).value

    HomeBody(
        taskList = allList,
        onClick = {task ->
            navigateToRunTimer(navController, task)
        },
        onFABClick = { id ->
            navigateToEditDialog(navController, id)
        }
    )
}

/**
 * Helper function to convert [Task] to a json string that will be sent to
 * the Run Timer Screen.
 *
 * @param task a [Task] that the user clicked
 * */
private fun navigateToRunTimer(
    navController: NavController,
    task: Task
) {
    val timerJson = Gson().toJson(task)

    // Navigate to the Run Timer screen
    navController.navigate(route = "${PomodoroScreen.RunTimer}/$timerJson") {
        launchSingleTop = true
    }
}

/**
 *
 * @param navController
 * @param id the id of the timer being edited
 * */
private fun navigateToEditDialog(
    navController: NavController,
    id: Long
) {
    navController.navigate(route = "${PomodoroScreen.EditDialog.name}/$id")
}

/**
 * Screen for the full dialog.
 *
 * */
@ExperimentalComposeUiApi
@Composable
fun CreateFullDialogScreen(
    navController: NavController,
    viewModel: EditTaskViewModel
) {
    EditTaskDialog(
        task = viewModel::currentTask.get().value!!,
        title = "Create New Pomodoro Task",
        onDismiss = { navController.popBackStack() },
        onSaveSessionLen = viewModel::setSessionLength,
        onSaveNumSessions = viewModel::setNumberSessions,
        onSaveShortLength = viewModel::setShortBreakLen,
        onSaveLongLength = viewModel::setLongBreakLen,
        onSaveName = viewModel::onSaveName,
        onSaveTask = viewModel::saveTask
    )
}

/**
 * Screen for the pomodoro timer.
 *
 * @param navController
 * @param runTimerViewModel the view model used by the screen
 * */
@Composable
fun TimerScreen(
    navController: NavController,
    runTimerViewModel: RunTimerViewModel,
    task: Task
) {
//    RunTimerScreen(
//        task = ,
//        timerStatus = runTimerViewModel.timerStatus.value,
//        timerType = runTimerViewModel.timerType.value,
//        timerString = runTimerViewModel.currentTimeString.value!!,
//        onStartPause = runTimerViewModel::onStartPause,
//        onReset = runTimerViewModel::onReset,
//        onAutoStart = runTimerViewModel::onAutoStart
//    )
}