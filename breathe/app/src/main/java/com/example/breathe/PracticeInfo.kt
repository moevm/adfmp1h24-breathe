package com.example.breathe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun PracticeInfoLayout(practiceNum: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier) {
            val practiceName : String = stringArrayResource(R.array.exercise_name)[practiceNum]
            BackHeader(practiceName)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PracticeInfoLayoutPreview() {
    BreatheTheme {
        PracticeInfoLayout(2, Modifier.fillMaxSize())
    }
}
