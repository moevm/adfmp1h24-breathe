package com.example.breathe

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme
import java.util.Locale

@Composable
fun HistoryCard(
    results: List<ProtoPracticeResult>,
    date: String,
    modifier: Modifier = Modifier
) {
    val totalTime = results.sumOf { it.resTotal }

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
            text = date,
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
            items(results.size) {
                Row {
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.scale(0.5F)
                    )
                    Text(
                        text = stringArrayResource(R.array.exercise_name)[results[it].id]
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
                text = SimpleDateFormat("mm", Locale.ENGLISH).format(totalTime * 1000L)
                     + " " + stringResource(id = R.string.min) + " "
                     + SimpleDateFormat("ss", Locale.ENGLISH).format(totalTime * 1000L)
                     + " " + stringResource(id = R.string.sec),
                modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
            )
        }
    }
}

@Composable
fun HistoryLayout(
    practices: List<ProtoPracticeResult>,
    modifier: Modifier = Modifier,
    upButton: (()->Unit)? = null,
    filter: String = "",
    filterChange: ((String)->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier)
        {
            BackHeader(title = stringResource(R.string.history), upButton = upButton)
            OutlinedTextField(
                value = filter,
                onValueChange = { if (filterChange != null) filterChange(it) },
                label = { Text(stringResource(R.string.filter_title)) },
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search)
            )
            val practiceGroups = mutableMapOf<Long, MutableList<ProtoPracticeResult>>()
            val secsInDay = 60 * 60 * 24

            val names = stringArrayResource(R.array.exercise_name)
            for (practice in practices) {
                val key = (practice.endDate.seconds / secsInDay) * secsInDay
                if (names[practice.id].contains(filter, true)) {
                    if (!practiceGroups.contains(key)) {
                        practiceGroups[key] = mutableListOf()
                    }
                    practiceGroups[key]?.add(practice)
                }
            }

            val dates = mutableListOf<String>()
            val results = mutableListOf<MutableList<ProtoPracticeResult>>()

            for (entry in practiceGroups) {
                dates.add(
                    SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
                        .format(entry.key * 1000L)
                )
                results.add(entry.value)
            }

            LazyColumn (
                verticalArrangement = Arrangement.Top,
                userScrollEnabled = true,
                modifier = modifier
            ) {
                itemsIndexed(results) { index, item ->
                    HistoryCard(
                        results = item,
                        date = dates[index]
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
        HistoryLayout(
            listOf(),
            Modifier.fillMaxSize()
        )
    }
}