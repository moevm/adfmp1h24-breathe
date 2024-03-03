package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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

class TrainingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                TrainingLayout(2, Modifier.fillMaxSize())
            }
        }
    }
}
@Composable
fun TrainingLayout(practiceNum: Int, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column (
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
        ) {
            Column (
                modifier = Modifier
            ) {
                TrainingHeader(practiceNum)
                TrainingTimer()
            }
            TrainingStopButton()
        }
    }
}

@Composable
fun TrainingTimer(modifier: Modifier = Modifier) {
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    Box (
        contentAlignment = Alignment.Center,
        modifier = Modifier
    ) {
        CircleBackground(
            integerArrayResource(R.array.background_circle_radius)[0],
            colorResource(R.color.timer_background_light),
            0.15F
        )
        CircleBackground(integerArrayResource(
            R.array.background_circle_radius)[1],
            colorResource(R.color.timer_background_light),
            0.3F
        )
        CircleBackground(integerArrayResource(
            R.array.background_circle_radius)[2],
            colorResource(R.color.timer_background_light),
            0.5F
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
            fontSize = 80.sp,
            modifier = Modifier
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text(
            text = unit,
            color = MaterialTheme.colorScheme.secondary,
            fontSize = 50.sp,
            modifier = Modifier
        )
    }
}

@Composable
fun CircleBackground(radius: Int, color: Color, alpha: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .size(radius.dp)
            .clip(CircleShape)
            .background(color)
            .alpha(alpha)
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

@Composable
fun TrainingStopButton(modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .clickable(onClick = {})
    ) {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        Text(
            text = stringResource(R.string.stop),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 10.dp, 0.dp, 10.dp)
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
