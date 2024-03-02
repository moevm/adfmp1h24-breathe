package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
        Column (
            modifier = modifier
        ) {
            Header(practiceNum)
            Text(
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall,
                text = stringArrayResource(R.array.exercise_desc)[practiceNum],
                fontSize = 18.sp,
                lineHeight = 24.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(15.dp, 10.dp, 15.dp, 5.dp)
            )
            Row (
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        modifier = Modifier.scale(2.5F),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                    EditNumberField(
                        value = "10.0"
                    )
                    IconButton(
                        modifier = Modifier.scale(2.5F),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                }
                Text (
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.min),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
                Column (
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(
                        modifier = Modifier.scale(2.5F),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                    EditNumberField(
                        value = "10.0"
                    )
                    IconButton(
                        modifier = Modifier.scale(2.5F),
                        onClick = { /*TODO*/ }
                    ) {
                        Icon(
                            Icons.Outlined.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                        )
                    }
                }
                Text (
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodySmall,
                    text = stringResource(R.string.sec),
                    fontSize = 18.sp,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                )
            }
            StartButton()
        }

    }
}

@Composable
fun Header(practiceNum: Int, modifier: Modifier = Modifier) {
    Row (
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
            text = stringArrayResource(R.array.exercise_name)[practiceNum],
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier.align(Alignment.CenterVertically)
        )
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { /*TODO*/ }
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

@Composable
fun StartButton(modifier: Modifier = Modifier) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 0.dp)
            .verticalScroll(rememberScrollState())
            .clickable(onClick = {})
    ) {
        Divider(color = MaterialTheme.colorScheme.outline, thickness = 1.dp)
        Text(
            text = stringResource(R.string.start),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(0.dp, 10.dp, 0.dp, 0.dp)
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    value: String,
    modifier: Modifier = Modifier
) {
    var number = value
    OutlinedTextField(
        value = number,
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        ),
        onValueChange = { newValue -> {number = newValue} },
        modifier = modifier
            .width(100.dp)
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PracticeLayoutPreview() {
    BreatheTheme {
        PracticeLayout(2, Modifier.fillMaxSize())
    }
}
