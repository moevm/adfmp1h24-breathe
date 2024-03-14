package com.example.breathe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun SettingsLayout(
    currentSettingsState: ProtoNotificationSettings,
    modifier: Modifier = Modifier,
    onNotifyChange: ((Boolean)->Unit)? = null,
    onHoursChange: ((Int)->Unit)? = null,
    onMinutesChange: ((Int)->Unit)? = null,
    aboutButton: (()->Unit)? = null,
    upButton: (()->Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackHeader(title = stringResource(R.string.settings), upButton = upButton)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 0.dp, 0.dp, 50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                    text = stringResource(R.string.notifications),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Switch(
                    checked = currentSettingsState.enabled,
                    onCheckedChange = {
                        if (onNotifyChange != null) onNotifyChange(it)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedBorderColor = MaterialTheme.colorScheme.outline,
                        checkedTrackColor = MaterialTheme.colorScheme.secondary,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
                    ),
                    modifier = Modifier.padding(0.dp, 0.dp, 50.dp, 0.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimeFieldWithText(
                    defaultValue = currentSettingsState.timeHours,
                    width = 80,
                    text = stringResource(R.string.hrs),
                    onValueChange = {
                        if (onHoursChange != null) onHoursChange(it)
                    }
                )
                TimeFieldWithText(
                    defaultValue = currentSettingsState.timeMinutes,
                    width = 80,
                    text = stringResource(R.string.min),
                    onValueChange = {
                        if (onMinutesChange != null) onMinutesChange(it)
                    }
                )
            }
            Button(
                onClick = { if (aboutButton != null) aboutButton() },
                modifier = Modifier.padding(50.dp, 120.dp, 50.dp, 0.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outline)
            ) {
                Text(
                    text = stringResource(R.string.about),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsLayoutPreview() {
    BreatheTheme {
        SettingsLayout(
            ProtoNotificationSettings.getDefaultInstance(),
            Modifier.fillMaxSize()
        )
    }
}