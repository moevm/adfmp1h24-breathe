package com.example.breathe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun SettingsHeader(title: String, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 20.dp)
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
            text = title,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(0.dp, 0.dp, 50.dp, 0.dp)
        )
    }
}

@Composable
fun SettingsLayout(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = modifier) {
            SettingsHeader(stringResource(R.string.settings))
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.notifications),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                var checked by remember { mutableStateOf(true) }
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = MaterialTheme.colorScheme.outline,
                        checkedTrackColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsLayoutPreview() {
    BreatheTheme {
        SettingsLayout(Modifier.fillMaxSize())
    }
}