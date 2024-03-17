package com.example.breathe

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.breathe.ui.theme.BreatheTheme

@Composable
fun ProfileLayout(
    profile: ProtoProfile,
    results: List<ProtoPracticeResult>,
    modifier: Modifier = Modifier,
    historyButton: (()->Unit)? = null,
    upButton: (()->Unit)? = null
) {
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background
    ) {
        val chartLabels: List<String> = stringArrayResource(R.array.chart_labels).asList()
        val values = mutableListOf<Float>()
        val expected = mutableListOf<Float>()
        for (i in 0..<4) {
            values.add(results.sumOf { it.resPhaseTimesList[i].toDouble() }.toFloat() / results.size)
            expected.add(results.sumOf { it.phaseTimesList[i].toDouble() }.toFloat() / results.size)
        }

        val scorePerLevel = 1000
        val level = profile.score / scorePerLevel
        val currentScore = profile.score % scorePerLevel

        val predicate = { name: String ->
            profile.achievementsList
                .find { achievement -> (achievement.name == name) }
                ?.active == true
        }
        val activeAchievements = stringArrayResource(R.array.achievements_list).filter( predicate )
        val inactiveAchievements = stringArrayResource(R.array.achievements_list).filter{ !predicate(it) }

        Column(
            modifier = modifier
        ) {
            BackHeaderWithButton(
                stringResource(R.string.profile_title),
                upButton = upButton,
                secondButton = historyButton
            )
            ProgressBarWithText(
                currentScore / scorePerLevel.toFloat(),
                0.toString(),
                scorePerLevel.toString(),
                headerText = stringResource(R.string.level_header, level)
            )
            AchievementsCard(
                activeAchievements,
                inactiveAchievements,
                headerText = stringResource(R.string.achievements_title)
            )
            StatisticsChart(
                chartLabels,
                values,
                expected,
                4,
                headerText = stringResource(R.string.total_results_header)
            )
        }
    }
}

@Preview
@Composable
fun ProfileLayoutPreview() {
    BreatheTheme {
        ProfileLayout(
            ProtoProfile.getDefaultInstance(),
            listOf(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
