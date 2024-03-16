package com.example.breathe

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.breathe.ui.theme.BreatheTheme
import dagger.hilt.android.AndroidEntryPoint

enum class BreatheScreen {
    PracticesList,
    Practice,
    Training,
    TrainingResult,
    Settings,
    Profile,
    History,
    PracticeInfo,
    About
}

@Composable
fun BreatheApp(
    viewModel: BreatheViewModel = viewModel()
) {

    val navController = rememberNavController()
    val settingsState by viewModel.settingsFlow.collectAsState(ProtoNotificationSettings.getDefaultInstance())
    val practiceState by viewModel.practiceState.collectAsState()
    val resultsState by viewModel.resultsFlow.collectAsState(ProtoPracticeResultList.getDefaultInstance())
    val profileState by viewModel.profileFlow.collectAsState(ProtoProfile.getDefaultInstance())

    NavHost(
        navController = navController,
        startDestination = BreatheScreen.PracticesList.name,
        modifier = Modifier.fillMaxSize()
    ) {
        val practiceNumArg = "practiceNum"
        val practiceNumUrl = "/{$practiceNumArg}"
        val arguments = listOf(
            navArgument(practiceNumArg) {
                type = NavType.IntType
                nullable = false
            }
        )

        composable(BreatheScreen.PracticesList.name) {
            PracticesListLayout(
                settingsButton = { navController.navigate(BreatheScreen.Settings.name) },
                profileButton = { navController.navigate(BreatheScreen.Profile.name) },
                onPracticeSelected = { seconds, phaseTimes ->
                    viewModel.setSettingsState(seconds, phaseTimes)
                },
                practiceButton = { num ->
                    navController.navigate(BreatheScreen.Practice.name + "/$num")
                }
            )
        }
        composable(BreatheScreen.Practice.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeLayout(
                    practiceNum = it,
                    currentPracticeState = practiceState,
                    onMinutesChanged = { value -> viewModel.setPracticeMinutes(value) },
                    onSecondsChanged = { value -> viewModel.setPracticeSeconds(value) },
                    onPhaseTimeChanged = { phase, time ->
                        viewModel.setPracticePhaseTime(phase, time)
                    },
                    infoButton = { num -> navController.navigate(
                        BreatheScreen.PracticeInfo.name + "/$num") },
                    startButton = { num ->
                        navController.navigate(BreatheScreen.Training.name + "/$num") {
                            popUpTo(BreatheScreen.PracticesList.name)
                        } },
                    upButton = { navController.navigateUp() }
                )
            }
        }
        composable(BreatheScreen.Training.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                val goToResult = {
                    navController.navigate(BreatheScreen.TrainingResult.name + "/$it") {
                        popUpTo(BreatheScreen.PracticesList.name)
                    }
                }
                TrainingLayout(
                    practiceNum = it,
                    currentPracticeState = practiceState,
                    stopButton = {
                        viewModel.timerEnd(it, practiceState)
                        goToResult()
                    },
                    onTimerTick = { viewModel.timerTick() },
                    onTimerEnd = {
                        viewModel.timerEnd(it, practiceState)
                        goToResult()
                    }
                )
            }
        }
        composable(
            BreatheScreen.TrainingResult.name + practiceNumUrl,
            arguments = arguments
        ) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                TrainingResultLayout(
                    practiceNum = it,
                    state = practiceState,
                    mainScreenButton = {
                        navController.navigate(BreatheScreen.PracticesList.name) {
                            popUpTo(BreatheScreen.PracticesList.name)
                        }
                    }
                )
            }
        }
        composable(BreatheScreen.Settings.name) {
            SettingsLayout(
                currentSettingsState = settingsState,
                onNotifyChange = { viewModel.saveNotifications(it) },
                onHoursChange = { viewModel.saveNotifyTimeHours(it) },
                onMinutesChange = { viewModel.saveNotifyTimeMinutes(it) },
                aboutButton = { navController.navigate(BreatheScreen.About.name) },
                upButton = { navController.navigateUp() }
            )
        }
        composable(BreatheScreen.Profile.name) {
            ProfileLayout(
                profileState,
                resultsState.resultsList,
                historyButton = { navController.navigate(BreatheScreen.History.name) },
                upButton = { navController.navigateUp() }
            )
        }
        composable(BreatheScreen.History.name) {
            HistoryLayout(
                resultsState.resultsList,
                upButton = { navController.navigateUp() }
            )
        }
        composable(BreatheScreen.PracticeInfo.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeInfoLayout(
                    practiceNum = it,
                    currentPracticeState = practiceState,
                    startButton = { num ->
                        navController.navigate(BreatheScreen.Training.name + "/$num") },
                    upButton = { navController.navigateUp() }
                )
            }
        }
        composable(BreatheScreen.About.name) {
            AboutLayout(upButton = { navController.navigateUp() })
        }
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = BreatheViewModel(DataManager(applicationContext))
        setContent {
            BreatheTheme {
                BreatheApp(viewModel)
            }
        }
    }
}
