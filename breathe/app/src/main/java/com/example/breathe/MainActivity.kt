package com.example.breathe

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.breathe.ui.theme.BreatheTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                CardsLayout(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun PracticeCard(practiceNum: Int, modifier: Modifier = Modifier) {
    OutlinedCard(
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            text = stringArrayResource(R.array.exercise_name)[practiceNum],
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(0.dp, 3.dp, 0.dp, 5.dp)
        )
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            text = stringArrayResource(R.array.exercise_desc)[practiceNum],
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(15.dp, 10.dp, 15.dp, 5.dp)
        )
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            text = integerArrayResource(R.array.exercise_time)[practiceNum].toString()
                + " " + stringResource(R.string.min),
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 10.dp, 15.dp, 5.dp)
        )
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }) {
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                text = stringResource(R.string.select),
            )
        }
    }
}

@Composable
fun CardsLayout(modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        val numbers = (0..3).toList()
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.Top,
            horizontalArrangement = Arrangement.SpaceEvenly,
            userScrollEnabled = true,
            modifier = modifier
        ) {
            items(numbers.size) {
                PracticeCard(
                    practiceNum = it,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardsLayoutPreview() {
    BreatheTheme {
        CardsLayout(Modifier.fillMaxSize())
    }
}