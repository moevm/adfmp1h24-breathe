package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.breathe.ui.theme.BreatheTheme

class PracticeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                PracticeLayout(2, Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun PracticeLayout(practiceNum: Int, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            text = stringArrayResource(R.array.exercise_desc)[practiceNum],
            fontSize = 18.sp,
            lineHeight = 24.sp,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(15.dp, 10.dp, 15.dp, 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PracticeLayoutPreview() {
    BreatheTheme {
        PracticeLayout(2, Modifier.fillMaxSize())
    }
}
