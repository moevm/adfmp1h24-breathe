package com.example.breathe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun PracticeCard(
    practiceNum: Int,
    modifier: Modifier = Modifier,
    onPracticeSelected: ((Int, IntArray)->Unit)? = null,
    practiceButton: ((Int)->Unit)? = null
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
        Row()
        {
            Icon(
                painter = painterResource(R.drawable.clock),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(15.dp, 10.dp, 0.dp, 10.dp)
                    .scale(0.8F)
            )
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                text = integerArrayResource(R.array.exercise_time)[practiceNum].toString()
                        + " " + stringResource(R.string.min),
                modifier = Modifier
                    .padding(5.dp, 10.dp, 15.dp, 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }
        val practiceIds = arrayOf(R.array.phase_times_1, R.array.phase_times_2,
            R.array.phase_times_3, R.array.phase_times_4)
        val practiceTime = integerArrayResource(R.array.exercise_time)[practiceNum]
        val phaseTimes = integerArrayResource(practiceIds[practiceNum])
        FooterButton(stringResource(R.string.select)) {
            if (onPracticeSelected != null) {
                onPracticeSelected(practiceTime, phaseTimes)
            }
            if (practiceButton != null) {
                practiceButton(practiceNum)
            }
        }
    }
}

@Composable
fun MainHeader(
    modifier: Modifier = Modifier,
    settingsButton: (()->Unit)? = null,
    profileButton: (()->Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { if (settingsButton != null) settingsButton() }
        ) {
            Icon(
                Icons.Outlined.Settings,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier
            )
        }
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { if (profileButton != null) profileButton() }
        ) {
            Icon(
                Icons.Outlined.AccountCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier
            )
        }
    }
}

@Composable
fun PracticesListLayout(
    modifier: Modifier = Modifier,
    onPracticeSelected: ((Int, IntArray)->Unit)? = null,
    settingsButton: (()->Unit)? = null,
    profileButton: (()->Unit)? = null,
    practiceButton: ((Int)->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier)
        {
            MainHeader(settingsButton = settingsButton, profileButton = profileButton)
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
                        modifier = modifier,
                        practiceButton = practiceButton,
                        onPracticeSelected = onPracticeSelected
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PracticesListLayoutPreview() {
    BreatheTheme {
        PracticesListLayout(Modifier.fillMaxSize())
    }
}
