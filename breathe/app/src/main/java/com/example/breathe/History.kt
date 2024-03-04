package com.example.breathe

import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ArrayRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

class History : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                HistoryLayout(Modifier.fillMaxSize())
            }
        }
    }
}

@Composable
fun HistoryCard(
    datesNum: Int, training: Array<Int>, time: Array<Int>, modifier: Modifier = Modifier
) {
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
            textAlign = TextAlign.Left,
            text = stringArrayResource(R.array.dates)[datesNum],
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .padding(15.dp, 3.dp, 0.dp, 5.dp)
        )
        LazyColumn (
            verticalArrangement = Arrangement.Top,
            userScrollEnabled = true,
            modifier = modifier
                .height(60.dp)
                .padding(15.dp, 10.dp, 15.dp, 5.dp)
        ) {
            items(training.size) {
                Row (

                ) {
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.scale(0.5F)
                    )
                    Text(
                        text = stringArrayResource(R.array.exercise_name)[training[it]]
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
        )
        {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
            )
            Text(
                text = time[0].toString() + " мин " + time[1].toString() + " с",
                modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
            )
        }
    }
}

@Composable
fun HistoryHeader(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { /*TODO*/ }
        ) {
            Icon(
                Icons.Outlined.ArrowBack,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier
            )
        }
        Text(
            text = stringResource(R.string.history),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        Spacer(
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
fun HistoryLayout(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier)
        {
            HistoryHeader()
            val numbers = (0..3).toList()
            val trainings = arrayOf(
                arrayOf(1, 2),
                arrayOf(0, 3),
                arrayOf(0, 2),
                arrayOf(1)
            )
            val trainingTimes = arrayOf(
                arrayOf(42, 14),
                arrayOf(17, 1),
                arrayOf(97, 45),
                arrayOf(0, 19)
            )
            LazyColumn (
                verticalArrangement = Arrangement.Top,
                userScrollEnabled = true,
                modifier = modifier
            ) {
                items(numbers.size) {
                    HistoryCard(
                        datesNum = it,
                        training = trainings[it],
                        time = trainingTimes[it],
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryLayoutPreview() {
    BreatheTheme {
        HistoryLayout(Modifier.fillMaxSize())
    }
}