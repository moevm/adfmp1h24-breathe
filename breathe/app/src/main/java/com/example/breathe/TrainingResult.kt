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
fun TrainingResult(
    practiceNum: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        val chartLabels: List<String> = stringArrayResource(R.array.chart_labels).asList()
        val values = listOf(3.0f, 4.0f, 6.0f, 8.0f)
        val expected = listOf(4.0f, 4.0f, 4.0f, 4.0f)

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = modifier
        ) {
            Column {
                TrainingHeader(practiceNum)
                StatisticsChart(
                    chartLabels,
                    values,
                    expected,
                    4,
                    headerText = stringResource(R.string.training_results_header)
                )
            }
            FooterButton(stringResource(R.string.to_main_screen)) {
                /*TODO*/
            }
        }
    }
}

@Preview
@Composable
fun TrainingResultPreview() {
    BreatheTheme {
        TrainingResult(1, modifier = Modifier.fillMaxSize())
    }
}
