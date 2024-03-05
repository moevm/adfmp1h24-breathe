package com.example.breathe

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun TitledImage(title: String, painter: Painter, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            text = title,
            modifier = modifier.padding(35.dp, 0.dp, 0.dp, 20.dp)
        )
        Image(
            painter = painter,
            contentDescription = null,
            alignment = Alignment.Center,
            modifier = Modifier.size(150.dp, 150.dp)
        )
    }
}

@Composable
fun TimeWithTitle(time: Int, title: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start,
            text = title
        )
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Start,
            text = time.toFloat().toString() + " " + stringResource(R.string.sec)
        )
    }
}

@Composable
fun PracticeInfoLayout(
    practiceNum: Int,
    modifier: Modifier = Modifier,
    startButton: ((Int)->Unit)? = null,
    upButton: (()->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier) {
            val practiceName: String = stringArrayResource(R.array.exercise_name)[practiceNum]
            val practiceText: String = stringArrayResource(R.array.exercise_desc)[practiceNum]
            val practiceMinutes: Int =  integerArrayResource(R.array.exercise_time)[practiceNum]
            val breathTime: Int = integerResource(R.integer.breathe_time)
            val practiceTime: String = practiceMinutes.toString() + " " +
                    stringResource(R.string.min) + " 0 " + stringResource(R.string.sec)
            val textMod : Modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 0.dp, 15.dp, 20.dp)
            BackHeader(title = practiceName, upButton = upButton)
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Start,
                text = stringResource(R.string.technique),
                modifier = textMod
            )
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Justify,
                text = practiceText,
                modifier = textMod
            )
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Start,
                text = stringResource(R.string.warning),
                modifier = textMod
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxWidth()
            ) {
                TitledImage(stringResource(R.string.no_breathe),
                    painterResource(R.drawable.no_breathe))
                TitledImage(stringResource(R.string.yes_breathe),
                    painterResource(R.drawable.yes_breathe))
            }
            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp, 20.dp, 0.dp, 20.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.clock),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .padding(15.dp, 10.dp, 10.dp, 10.dp)
                        .scale(0.8F)
                )
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    text = practiceTime,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TimeWithTitle(breathTime, stringResource(R.string.breath_in))
                TimeWithTitle(breathTime, stringResource(R.string.hold))
                TimeWithTitle(breathTime, stringResource(R.string.breath_out))
                TimeWithTitle(breathTime, stringResource(R.string.hold))
            }
            FooterButton(text = stringResource(R.string.start), offset = 110) {
                if (startButton != null) {
                    startButton(practiceNum)
                }
            }
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
