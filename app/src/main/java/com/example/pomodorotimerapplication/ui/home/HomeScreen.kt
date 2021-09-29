package com.example.pomodorotimerapplication.ui.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import com.example.pomodorotimerapplication.R
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodorotimerapplication.data.Task
import com.example.pomodorotimerapplication.ui.theme.GreenLight
import com.example.pomodorotimerapplication.ui.theme.PomodoroTimerApplicationTheme
import com.example.pomodorotimerapplication.ui.theme.Shapes
import com.example.pomodorotimerapplication.utilities.isScrollingUp

/**
 *
 * @param taskList (state) list of [Task] to display some information about each task
 * @param onClick (event) request to navigate to next screen
 * */
@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Composable
fun HomeBody(
    taskList: List<Task>,
    onClick: (Task) -> Unit,
    onFABClick: () -> Unit
){
    val lazyListState = rememberLazyListState()

    // TODO: Add a full-screen dialog to create a new pomodoro task

    PomodoroTimerApplicationTheme {
        Scaffold(
            modifier = Modifier.semantics { contentDescription = "Home Screen" },
            topBar = {},
            floatingActionButton = {
                // Show FAB
                CreateFloatingActionButton(
                    extended = lazyListState.isScrollingUp(),
                    onClick = onFABClick
                )
            },
            floatingActionButtonPosition = FabPosition.End
        ) {
            GreetingCard()
            if(taskList.isEmpty()){
                HomeEmptyContent()
            }else{
                HomeBodyContent(
                    taskList,
                    lazyListState,
                    onClick
                )
            }
        }
    }
}

@Composable
fun HomeEmptyContent(){
    Column {
        Row(
            Modifier
                .padding(top = 150.dp, start = 100.dp)
        ) {
            Text(
                text = "No Tasks",
                style = MaterialTheme.typography.h3
            )
        }
        Row(
            Modifier
                .padding(start = 40.dp, top = 20.dp)
        ) {
            Text(
                text = "Click the Button to create a new Task.",
                style = MaterialTheme.typography.h5
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun HomeBodyContent(
    taskList: List<Task>,
    state: LazyListState,
    onClick: (Task) -> Unit
){

    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp),
        state = state
    ){
        items(taskList) { task ->
            TaskCard(
                task = task,
                onClick = onClick
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GreetingCard(){
    // Get current time of device
    //var currentTime = LocalDateTime.now()

//    val greetingText = when(DateTimeFormatter.ofPattern("HH").toString()){
//        ""
//    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = Shapes.large,
                elevation = 12.dp,
                backgroundColor = GreenLight
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_back_greeting),
                    fontSize = MaterialTheme.typography.h5.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}

/**
 * Displays the name of the task. It can be expanded to show more information about the task.
 *
 * @param task A [Task] that the user saved previously
 * */
@ExperimentalMaterialApi
@Composable
fun TaskCard(
    task: Task,
    onClick: (Task) -> Unit
){
    var expandableState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if(expandableState) 180f else 0f)
    
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
        ,
        shape = Shapes.medium,
        elevation = 8.dp,
        border = BorderStroke(1.dp, Color.Black),
        onClick = {
            expandableState = !expandableState
        }
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            //.padding(8.dp)
        ) {
            Surface(color = MaterialTheme.colors.primaryVariant){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .weight(6f)
                            .padding(start = 8.dp)
                        ,
                        text = task.taskName,
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium)
                            .weight(1f)
                            .rotate(rotationState),
                        onClick = {
                            expandableState = !expandableState
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            }
            if(expandableState){
                ExpandedTaskCard(
                    task = task,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun ExpandedTaskCard(
    task: Task,
    onClick: (Task) -> Unit
){
    Column(modifier = Modifier
        .fillMaxWidth()
        //.padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = stringResource(id = R.string.task_status_string, task.taskStatus),
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondaryVariant)
            ,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                modifier = Modifier
                    .alpha(ContentAlpha.medium)
                ,
                onClick = {
                    onClick.invoke(task)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}

/**
 * Shows a floating action button (FAB)
 *
 * @param extended (state) extends/collapses the FAB
 * @param onClick (event) notify caller that the FAB was clicked
 * */
@ExperimentalAnimationApi
@Composable
private fun CreateFloatingActionButton(
    extended: Boolean,
    onClick: () -> Unit
){
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 48.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Edit, contentDescription = null)

            // Toggle the visibility of the content with animation
            AnimatedVisibility(
                visible = extended
            ) {
                Text(
                    text = "Create",
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewTaskCard(){
    val task = Task(1, "Study for Physics III test", 25,
        5, 15, 4, "In Progress")
    TaskCard(
        task = task,
        onClick = {}
    )
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewEmptyHomeBody(){
    HomeBody(
        taskList = emptyList(),
        onClick = {},
        onFABClick = {}
    )
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterialApi
@Preview
@Composable
fun PreviewHomeBody(){
    HomeBody(
        taskList = listOf(
            Task(1, "Study for Physics III test", 25,
                5, 15, 4),
            Task(2, "Work on Algorithms Homework", 25,
                5, 15, 3),
            Task(3, "Review Compose library", 30,
                10, 20, 4)
        ),
        onClick = {},
        onFABClick = {}
    )
}
