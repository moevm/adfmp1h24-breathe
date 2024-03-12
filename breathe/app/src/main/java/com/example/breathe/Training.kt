package com.example.breathe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathe.ui.theme.BreatheTheme
import kotlinx.coroutines.delay

@Composable
fun TrainingLayout(
    practiceNum: Int,
    modifier: Modifier = Modifier,
    currentPracticeState: BreathePracticeState = BreathePracticeState(),
    onTimerTick: (()->Unit)?=null,
    onTimerEnd: (()->Unit)?=null,
    stopButton: (()->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
            ) {
                TrainingHeader(practiceNum)
                TrainingTimer(
                    currentPracticeState = currentPracticeState,
                    onTimerTick = onTimerTick,
                    onTimerEnd = onTimerEnd
                )
            }
            FooterButton(text = stringResource(R.string.stop), offset = 30) {
                if (stopButton != null) {
                    stopButton()
                }
            }
        }
    }
}

@Composable
fun TrainingTimer(
    currentPracticeState: BreathePracticeState,
    modifier: Modifier = Modifier,
    onTimerTick: (()->Unit)?=null,
    onTimerEnd: (()->Unit)?=null
) {
    val minutes = currentPracticeState.currentSeconds / 60
    val seconds = currentPracticeState.currentSeconds % 60
    LaunchedEffect(currentPracticeState.currentSeconds) {
        delay(1000)
        if (currentPracticeState.currentSeconds <= 0 && onTimerEnd != null) {
            onTimerEnd()
        }
        else if (onTimerTick != null) {
            onTimerTick()
        }
    }


    Box (
        contentAlignment = Alignment.Center,
        modifier = modifier
            .height(600.dp)
            .fillMaxWidth()
    ) {
        CircleBackground(
            integerArrayResource(R.array.background_circle_radius)[0],
            colorResource(R.color.timer_background_dark)
        )
        CircleBackground(integerArrayResource(
            R.array.background_circle_radius)[1],
            colorResource(R.color.timer_background_medium)
        )
        CircleBackground(integerArrayResource(
            R.array.background_circle_radius)[2],
            colorResource(R.color.timer_background_light)
        )
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerDisplay(minutes, stringResource(R.string.min))
            TimerDisplay(seconds, stringResource(R.string.sec))
        }
    }
}

@Composable
fun TimerDisplay(time: Int, unit: String, modifier: Modifier = Modifier) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text (
            text = time.toString(),
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 70.sp,
            modifier = modifier
        )
        Spacer(modifier = modifier.size(10.dp))
        Text(
            text = unit,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 40.sp,
            modifier = modifier
        )
    }
}

@Composable
fun CircleBackground(radius: Int, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(radius.dp)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun TrainingHeader(practiceNum: Int, modifier: Modifier = Modifier) {
    Row (
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringArrayResource(R.array.exercise_name)[practiceNum],
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TrainingLayoutPreview() {
    BreatheTheme {
        TrainingLayout(2, Modifier.fillMaxSize())
    }
}
