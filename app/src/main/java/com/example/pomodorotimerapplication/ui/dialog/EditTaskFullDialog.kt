package com.example.pomodorotimerapplication.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pomodorotimerapplication.data.Task
import com.example.pomodorotimerapplication.R

/**
 * Shows a full screen dialog where the user can edit a task's initial options.
 *
 * @param task (state) a [Task] that can be passed in (can be null if it's a new task)
 * @param onDismiss (event) request that the dialog be dismissed
 * */
@ExperimentalComposeUiApi
@Composable
fun EditTaskDialog(
    task: Task,
    title: String,
    onDismiss: () -> Unit,
    onSaveSessionLen: (Int) -> Unit,
    onSaveNumSessions: (Int) -> Unit,
    onSaveShortLength: (Int) -> Unit,
    onSaveLongLength: (Int) -> Unit,
    onSaveName: (String) -> Unit,
    onSaveTask: () -> Unit

){
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier.fillMaxHeight()
        ) {
            Column {
                // 'Top bar' where user can close dialog
                Row(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .alpha(1f)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close the dialog"
                        )
                    }
                    Text(
                        modifier = Modifier
                            .weight(6f)
                            .padding(start = 8.dp)
                        ,
                        text = title,
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            DialogContent(
                task = task,
                onSaveSessionLen = onSaveSessionLen,
                onSaveNumSessions = onSaveNumSessions,
                onSaveShortLength = onSaveShortLength,
                onSaveLongLength = onSaveLongLength
            )

            // Row of buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorResource(id = R.color.green_dark))
            ) {
               TextButton(
                   onClick = onDismiss,
                   modifier = Modifier.weight(1f)
               ) {
                   Text(
                       text = "Cancel"
                   )
               }
                TextButton(
                    onClick = {
                        onSaveTask.invoke()
                        onDismiss.invoke()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Submit"
                    )
                }
            }
        }
    }
}

@Composable
fun DialogContent(
    task: Task,
    onSaveSessionLen: (Int) -> Unit,
    onSaveNumSessions: (Int) -> Unit,
    onSaveShortLength: (Int) -> Unit,
    onSaveLongLength: (Int) -> Unit
){

    Box(
        modifier = Modifier,
        contentAlignment = Alignment.Center
    ){
        // Session Edit block
        EditBlock(
            lengthText = stringResource(id = R.string.session_length),
            numberText = stringResource(id = R.string.number_of_sessions),
            timerLength = task.sessionLen,
            numLength = task.numOfSessions,
            timerRange = intArrayOf(5, 60),
            numberRange = intArrayOf(2,10),
            onSaveTimeValue = onSaveSessionLen,
            onSaveNumValue = onSaveNumSessions
        )
        
        // Short Break Edit Block
        EditBlock(
            lengthText = stringResource(id = R.string.short_break_length),
            timerLength = task.shortBreakLen,
            timerRange = intArrayOf(5, 60),
            onSaveTimeValue = onSaveShortLength,
            onSaveNumValue = {}
        )
        
        //Long Break Edit Block
        EditBlock(
            lengthText = stringResource(id = R.string.long_break_length),
            timerLength = task.longBreakLen,
            timerRange = intArrayOf(5, 60),
            onSaveTimeValue = onSaveLongLength,
            onSaveNumValue = {}
        )
    }
}

/**
 *
 * @param timerLength the current set length of the timer
 * @param numLength the current set number of times the timer will be used
 * @param timerRange an array of the range for the timer
 * @param numberRange an array of the range for the number
 * */
@Composable
fun EditBlock(
    lengthText: String,
    numberText: String? = null,
    timerLength: Int,
    numLength: Int? = null,
    timerRange: IntArray,
    numberRange: IntArray? = null,
    onSaveTimeValue: (Int) -> Unit,
    onSaveNumValue: (Int) -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Divider(color = Color.Black, thickness = 1.dp)
    }
    EditRow(
        text = lengthText,
        numLength = timerLength,
        arrRange = timerRange,
        onSaveValue = onSaveTimeValue
    )
    if(numberText != null){
        EditRow(
            text = numberText,
            numLength = numLength!!,
            arrRange = numberRange!!,
            onSaveValue = onSaveNumValue
        )
    }
}

/**
 * Allows user to edit a value using a dropdown menu.
 *
 * @param text a String that displays what the number is affecting
 * @param numLength (state) the current value
 * @param arrRange an IntArray representing the range of values the user can select from
 * @param onSaveValue (event) request that the selected value be saved
 * */
@Composable
fun EditRow(
    text: String,
    numLength: Int,
    arrRange: IntArray,
    onSaveValue: (Int) -> Unit
){
    val expanded = remember { mutableStateOf(false)}

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(4f)
        )
        Text(
            text = numLength.toString(),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
        NumberDropDown(
            lowVal = arrRange[0],
            highVal = arrRange[1],
            selectedVal = numLength,
            expanded = expanded.value,
            //onClick = { expanded.value = true },
            onDismiss = { expanded.value = false },
            onSelect = onSaveValue,
            modifier = Modifier
                .requiredSizeIn(maxHeight = 200.dp)
                .weight(1f)
        )
        IconButton(
            onClick = { expanded.value = true }
        ) {
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewEditRow(){
    EditRow(
        text = stringResource(id = R.string.session_length),
        numLength = 25,
        arrRange = intArrayOf(5, 60),
        onSaveValue = {}
    )
}

@ExperimentalComposeUiApi
@Preview
@Composable
fun PreviewEditTaskDialog(){
    EditTaskDialog(
        task = Task(1, "Study for Physics III test", 25,
        5, 15, 4),
        title = "Create New Pomodoro Timer",
        onDismiss = { },
        onSaveSessionLen = { },
        onSaveNumSessions = { },
        onSaveShortLength = { },
        onSaveLongLength = { },
        onSaveName = { },
        onSaveTask = { }
    )
}
