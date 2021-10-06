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
import com.example.pomodorotimerapplication.ui.theme.PomodoroTimerApplicationTheme
import com.example.pomodorotimerapplication.utilities.PomodoroScreen
import com.example.pomodorotimerapplication.viewmodel.EditTaskViewModel
import com.example.pomodorotimerapplication.viewmodel.EditTaskViewModelFactory
import com.example.pomodorotimerapplication.viewmodel.HomeViewModel
import com.example.pomodorotimerapplication.viewmodel.HomeViewModelFactory
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
        val homeViewModel = ViewModelProvider(this, homeViewModelFactory)
            .get(HomeViewModel::class.java)

        // Set up view model for Edit Task Full Dialog
        val editTaskViewModelFactory = EditTaskViewModelFactory(0L, homeViewModel.repository)
        val editTaskViewModel = ViewModelProvider(this, editTaskViewModelFactory)
            .get(EditTaskViewModel::class.java)

        setContent {
            PomodoroApp(
                homeViewModel = homeViewModel,
                editTaskViewModel = editTaskViewModel
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
    editTaskViewModel: EditTaskViewModel
){
    PomodoroTimerApplicationTheme {
        Surface {
            // TODO: Add loading screen

            val navController = rememberNavController()

            Scaffold { innerPadding ->
                PomodoroAppNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    editTaskViewModel = editTaskViewModel,
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
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = PomodoroScreen.Home.name,
        modifier = modifier
    ){
        composable(route = PomodoroScreen.Home.name){
            HomeScreen(
                navController = navController,
                homeViewModel = homeViewModel
            )
        }

        composable(
            route = "${PomodoroScreen.EditDialog.name}/{taskId}",
            arguments = listOf(
                navArgument("taskId"){
                    type = NavType.LongType
                }
            )
        ){ backStackEntry ->
           backStackEntry.arguments?.getLong("taskId", 0L).let {
               CreateFullDialogScreen(
                   navController = navController,
                   viewModel = editTaskViewModel
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
){
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
){
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
){
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
){
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