package com.example.breathe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EditNumberField(
    value: Int,
    width: Int,
    modifier: Modifier = Modifier,
    onValueChange: ((Int) -> Unit)? = null
) {
    OutlinedTextField(
        value = value.toString(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.outline,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
        ),
        onValueChange = {
            if (onValueChange != null) {
                val digits = it.filter{ c: Char -> c.isDigit() }
                if (digits.isNotEmpty()) {
                    onValueChange(digits.toInt())
                }
            } },
        modifier = modifier
            .width(width.dp)
            .padding(10.dp, 0.dp, 10.dp, 0.dp)
    )
}

@Composable
fun TimeFieldWithButtons(
    defaultValue: Int,
    width: Int,
    modifier: Modifier = Modifier,
    maxValue: Int = 59,
    minValue: Int = 0,
    onValueChange: ((Int) -> Unit)? = null
) {
    var value by remember { mutableIntStateOf(defaultValue) }
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = modifier.scale(2.5F),
            onClick = {
                if (value < maxValue) {
                    value += 1
                    if (onValueChange != null) {
                        onValueChange(value)
                    }
                }
            }
        ) {
            Icon(
                Icons.Outlined.KeyboardArrowUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = modifier
            )
        }
        EditNumberField(
            value = value,
            width = width,
            onValueChange = {
                if (it in minValue..maxValue) {
                    value = it
                    if (onValueChange != null) {
                        onValueChange(it)
                    }
                }
            }
        )
        IconButton(
            modifier = modifier.scale(2.5F),
            onClick = {
                if (value > minValue) {
                    value -= 1
                    if (onValueChange != null) {
                        onValueChange(value)
                    }
                }
            }
        ) {
            Icon(
                Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = modifier
            )
        }
    }
}

@Composable
fun TimeFieldWithText(
    defaultValue: Int,
    width: Int,
    text: String,
    modifier: Modifier = Modifier,
    maxValue: Int = 59,
    minValue: Int = 0,
    onValueChange: ((Int) -> Unit)? = null
) {
    Row {
        TimeFieldWithButtons(
            defaultValue = defaultValue,
            width = width,
            maxValue = maxValue,
            minValue = minValue,
            onValueChange = onValueChange
        )
        Text (
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            text = text,
            fontSize = 18.sp,
            modifier = modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun TimeFieldWithTitle(
    defaultValue: Int,
    width: Int,
    title: String,
    text: String,
    modifier: Modifier = Modifier,
    maxValue: Int = 59,
    minValue: Int = 0,
    onValueChange: ((Int) -> Unit)? = null
) {
    Column (
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(5.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodySmall,
            text = title,
            fontSize = 18.sp,
            modifier = modifier
                .align(Alignment.CenterHorizontally)
        )
        TimeFieldWithText(
            defaultValue = defaultValue,
            width = width,
            text = text,
            maxValue = maxValue,
            minValue = minValue,
            onValueChange = onValueChange
        )
    }
}