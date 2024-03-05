package com.example.breathe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.common.extensions.getMaxElementInYAxis
import co.yml.charts.ui.barchart.StackedBarChart
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.SelectionHighlightData

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

@Composable
fun BackHeader(
    title: String,
    modifier: Modifier = Modifier,
    upButton: (()->Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp, 0.dp, 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        IconButton(
            modifier = modifier.scale(1.6F),
            onClick = { if (upButton != null) upButton() }
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
fun FooterButton(
    text: String,
    offset: Int = 0,
    onClick: (()->Unit)? = null
) {
    Divider(
        color = MaterialTheme.colorScheme.outline,
        thickness = 2.dp,
        modifier = Modifier.padding(0.dp, offset.dp, 0.dp, 0.dp)
    )
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (onClick != null) {
                onClick()
            }
        }) {
        Text(
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.titleLarge,
            text = text,
        )
    }
}

@Composable
fun StatisticsChart(
    labels: List<String>,
    values: List<Float>,
    expected: List<Float>,
    yStepsCount: Int,
    modifier: Modifier = Modifier,
    headerText: String = ""
) {
    if ( labels.size != values.size || values.size != expected.size )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        ) {}
        return
    }
    val halfExpectedHeight = 0.05f
    val blue = colorResource(R.color.timer_background_light).copy(alpha = 0.8f)
    val white = Color.White.copy(alpha = 0.9f)
    val colorPaletteList = listOf(Color.Transparent, blue, white, blue )

    val stackedBarData = mutableListOf<GroupBar>()
    for (index in values.indices) {
        val singleBarData = mutableListOf<BarData>()
        if ( values[index] > expected[index] ) {
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = expected[index] - halfExpectedHeight)
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = 0f) // skip color
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = 2 * halfExpectedHeight)
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = values[index] - expected[index] - halfExpectedHeight)
            ))
        } else {
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = values[index] - halfExpectedHeight)
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = expected[index] - values[index] - halfExpectedHeight)
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = 2 * halfExpectedHeight)
            ))
            singleBarData.add(BarData(
                point = Point(x = index.toFloat(), y = 0f) // skip color
            ))
        }
        stackedBarData.add(GroupBar(index.toString(), singleBarData))
    }

    val xAxisData = AxisData.Builder()
        .steps(labels.size - 1)
        .startDrawPadding(50.dp)
        .backgroundColor(Color.Transparent)
        .shouldDrawAxisLineTillEnd(true)
        .indicatorLineWidth(0.dp)
        .axisLineColor(blue)
        .axisLabelFontSize(12.sp)
        .axisLabelColor(white)
        .labelData { index -> labels[index] }
        .build()
    val yAxisData = AxisData.Builder()
        .steps(yStepsCount)
        .backgroundColor(Color.Transparent)
        .indicatorLineWidth(0.dp)
        .axisLabelFontSize(12.sp)
        .axisLineColor(blue)
        .axisLabelColor(white)
        .labelData { index ->
            val valueList = mutableListOf<Float>()
            stackedBarData.map{ groupBar ->
                var yMax = 0f
                groupBar.barList.forEach{
                    yMax += it.point.y
                }
                valueList.add(yMax)
            }
            val maxElementInYAxis = getMaxElementInYAxis(valueList.maxOrNull() ?: 0f, yStepsCount)
            String.format("%.2f", (index * (maxElementInYAxis / yStepsCount.toFloat())))
        }
        .topPadding(36.dp)
        .build()
    val plotData = BarPlotData(
        groupBarList = stackedBarData,
        barStyle = BarStyle(
            barWidth = 50.dp,
            selectionHighlightData = SelectionHighlightData(
                isHighlightFullBar = true,
                groupBarPopUpLabel = { name, _ ->
                    val index = name.toInt()
                    " ${String.format("%.2f", values[index])} / ${String.format("%.2f", expected[index])} "
                },
                highlightTextBackgroundColor = blue,
                highlightTextColor = white,
                highlightBarColor = colorResource(R.color.timer_background_dark)
            )
        ),
        barColorPaletteList = colorPaletteList
    )
    val chartData = GroupBarChartData(
        barPlotData = plotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        drawBar = { drawScope, _, barStyle, drawOffset, height, barIndex ->
            with(drawScope) {
                drawRect(
                    color = colorPaletteList[barIndex],
                    topLeft = drawOffset,
                    size = Size(barStyle.barWidth.toPx(), height),
                    style = barStyle.barDrawStyle,
                    blendMode = barStyle.barBlendMode
                )
            }
        },
        backgroundColor = Color.Transparent
    )
    Column{
        if (headerText.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 4.dp)
            ) {
                Text(
                    text = headerText,
                    color = white,
                    fontSize = 18.sp
                )
                Icon(
                    Icons.Outlined.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(15.dp, 10.dp, 0.dp, 10.dp)
                )
            }
        }
        StackedBarChart(
            modifier = modifier.height(300.dp),
            groupBarChartData = chartData
        )
    }
}
