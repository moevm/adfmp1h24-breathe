package com.example.breathe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun PracticeLayout(
    practiceNum: Int,
    modifier: Modifier = Modifier,
    currentPracticeState: BreathePracticeState = BreathePracticeState(),
    onMinutesChanged: ((Int)->Unit)? = null,
    onSecondsChanged: ((Int)->Unit)? = null,
    onPhaseTimeChanged: ((Int, Int)->Unit)? = null,
    infoButton: ((Int)->Unit)? = null,
    startButton: ((Int)->Unit)? = null,
    upButton: (()->Unit)? = null
) {
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
                PracticeHeader(
                    practiceNum = practiceNum,
                    infoButton = infoButton,
                    upButton = upButton
                )
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringArrayResource(R.array.exercise_desc)[practiceNum],
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(15.dp, 10.dp, 15.dp, 5.dp)
                )
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.clock),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp).scale(0.8F)
                    )
                    TimeFieldWithText(
                        defaultValue = currentPracticeState.totalSeconds / 60,
                        width = 80,
                        text = stringResource(R.string.min),
                        minValue = 1,
                        onValueChange = onMinutesChanged
                    )
                    TimeFieldWithText(
                        defaultValue = currentPracticeState.totalSeconds % 60,
                        width = 80,
                        text = stringResource(R.string.sec),
                        onValueChange = onSecondsChanged
                    )
                }
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(0.dp, 20.dp, 0.dp, 0.dp)
                ) {
                    TimeFieldWithTitle(currentPracticeState.phaseTimes[0], 70,
                        stringResource(R.string.breath_in), stringResource(R.string.sec),
                        minValue = 1,
                        onValueChange = {
                            if (onPhaseTimeChanged != null) onPhaseTimeChanged(0, it)
                        })
                    TimeFieldWithTitle(currentPracticeState.phaseTimes[1], 70,
                        stringResource(R.string.hold), stringResource(R.string.sec),
                        minValue = 1,
                        onValueChange = {
                            if (onPhaseTimeChanged != null) onPhaseTimeChanged(1, it)
                        })
                    TimeFieldWithTitle(currentPracticeState.phaseTimes[2], 70,
                        stringResource(R.string.breath_out), stringResource(R.string.sec),
                        minValue = 1,
                        onValueChange = {
                            if (onPhaseTimeChanged != null) onPhaseTimeChanged(2, it)
                        })
                    TimeFieldWithTitle(currentPracticeState.phaseTimes[3], 70,
                        stringResource(R.string.hold), stringResource(R.string.sec),
                        minValue = 1,
                        onValueChange = {
                            if (onPhaseTimeChanged != null) onPhaseTimeChanged(3, it)
                        })
                }

            }
            FooterButton(text = stringResource(R.string.start), offset = 0) {
                if (startButton != null) {
                    startButton(practiceNum)
                }
            }
        }
    }
}

@Composable
fun PracticeHeader(
    practiceNum: Int,
    modifier: Modifier = Modifier,
    infoButton: ((Int)->Unit)? = null,
    upButton: (()->Unit)? = null
) {
    Row (
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { if (upButton != null) upButton() }
        ) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier
            )
        }
        Text(
            text = stringArrayResource(R.array.exercise_name)[practiceNum],
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { if (infoButton != null) infoButton(practiceNum) }
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PracticeLayoutPreview() {
    BreatheTheme {
        PracticeLayout(2, Modifier.fillMaxSize())
    }
}
