package com.example.pomodorotimerapplication.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 4000
private const val textAnimationDuration: Int = 500

@ExperimentalAnimationApi
@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    onTimeOut: () -> Unit
){
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        // This will always refer to the latest onTimeout function that
        // LoadingScreen was recomposed with
        val currentOnTimeout by rememberUpdatedState(newValue = onTimeOut)

        // Create an effect that matches the lifecycle of LoadingScreen
        // If LoadingScreen recomposes or onTimeout changes, the delay shouldn't start again
        LaunchedEffect(key1 = true){
            delay(SplashWaitTime)
            currentOnTimeout()
        }

        // Animate the text while app is loading
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = textAnimationDuration)),
            exit = fadeOut(animationSpec = tween(durationMillis = textAnimationDuration))
        ) {
            Text(
                text = "Loading...",
                fontSize = MaterialTheme.typography.h4.fontSize,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun PreviewLoadingScreen(){
    LoadingScreen(
        onTimeOut = {}
    )
}