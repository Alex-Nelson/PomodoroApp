package com.example.pomodorotimerapplication.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pomodorotimerapplication.R

/**
 * Stateful composable to allow entry of a new name
 *
 * @param name name of the pomodoro timer
 * @param onTextChange (event) notify caller of text change
 * @param onNameComplete (event) notify caller that a name has been entered
 * */
@ExperimentalComposeUiApi
@Composable
fun TaskNameInput(
    name: String,
    onTextChange: (String) -> Unit,
    onNameComplete: (String) -> Unit
){
    val submit = {
        if(name.isNotBlank()){
            onNameComplete(name)
            onTextChange(name)
        }
    }

    NameInputText(
        text = name,
        onTextChange = onTextChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
        ,
        onImeAction = submit,
        singleLine = true
    )
}

/**
 * Style [TextField] for inputting a name
 *
 * @param text (state) current text of the name
 * @param onTextChange (event) request the text change
 * @param modifier the modifier for this element
 * @param onImeAction (event) notify the caller of [ImeAction.Done] events
 * */
@ExperimentalComposeUiApi
@Composable
fun NameInputText(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onImeAction: () -> Unit = {},
    singleLine: Boolean
){
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = text,
        onValueChange = onTextChange,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {
            onImeAction()
            keyboardController?.hide()
        }),
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            cursorColor = Color.Black,
            unfocusedIndicatorColor = Color(R.color.green_dark)
        )
    )
}

/**
 *
 * @param lowVal an integer represents the lowest value for the menu
 * @param highVal an integer represents the highest value for the menu
 * @param selectedVal an integer that is the default value
 * @param expanded a boolean that expands/collapses the menu
 * @param onDismiss (event) notify caller to collapse the menu
 * @param onSelect (event) request that the selected number be saved
 * */
@Composable
fun NumberDropDown(
    lowVal: Int,
    highVal: Int,
    selectedVal: Int,
    expanded: Boolean,
    //onClick: () -> Unit,
    onDismiss: () -> Unit,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
){

    val rangeList = createNumberList(lowVal, highVal)

    Box {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismiss,
            modifier = modifier
        ) {
            rangeList.forEach { num ->
                // Set style for text
                val isSelected = num == selectedVal
                val style = if(isSelected){
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.secondary
                    )
                }else{
                    MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colors.onSurface
                    )
                }
                DropdownMenuItem(
                    onClick = {
                        onSelect.invoke(num)
                        onDismiss.invoke()
                    }
                ) {
                    Text(text = num.toString(), style = style)
                }
            }
        }
    }
}

/**
 * Helper function to create an integer list of values with a range [lowVal, highVal].
 *
 * @param lowVal the lowest value in the range (inclusive)
 * @param highVal the highest value in the range (inclusive)
 *
 * @return an integer list of the given range
 * */
private fun createNumberList(
    lowVal: Int,
    highVal: Int,
): List<Int>{
    val list = mutableListOf<Int>()

    for(i in (lowVal..highVal)){
        list.add(i)
    }

    return list
}

@Preview
@Composable
fun PreviewExpandedDropDownMenu(){
    NumberDropDown(
        lowVal = 1,
        highVal = 50,
        selectedVal = 25,
        expanded = true,
        onDismiss = {},
        //onClick = {},
        onSelect = {}
    )
}

@Preview
@Composable
fun PreviewCollapsedDropDownMenu(){
    NumberDropDown(
        lowVal = 1,
        highVal = 50,
        selectedVal = 25,
        expanded = false,
        onDismiss = {},
        //onClick = {},
        onSelect = {}
    )
}