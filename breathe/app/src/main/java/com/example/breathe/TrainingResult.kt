package com.example.breathe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun TrainingResultLayout(
    practiceNum: Int,
    state: BreathePracticeState,
    modifier: Modifier = Modifier,
    mainScreenButton: (() -> Unit)? = null
) {
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        val chartLabels: List<String> = stringArrayResource(R.array.chart_labels).asList()

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            Column {
                TrainingHeader(practiceNum)
                StatisticsChart(
                    chartLabels,
                    state.currentPhaseTimes.map{ it.toFloat() }.toList(),
                    state.phaseTimes.map{ it.toFloat() }.toList(),
                    4,
                    headerText = stringResource(R.string.training_results_header)
                )
            }
            FooterButton(stringResource(R.string.to_main_screen)) {
                if (mainScreenButton != null) {
                    mainScreenButton()
                }
            }
        }
    }
}

@Preview
@Composable
fun TrainingResultPreview() {
    BreatheTheme {
        TrainingResultLayout(
            1,
            BreathePracticeState(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
