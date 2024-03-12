package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    val settingsState by viewModel.settingsState.collectAsState()
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
                practiceButton = { num ->
                    navController.navigate(BreatheScreen.Practice.name + "/$num") }
            )
        }
        composable(BreatheScreen.Practice.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeLayout(
                    practiceNum = it,
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
                TrainingLayout(
                    practiceNum = it,
                    stopButton = { num ->
                        navController.navigate(BreatheScreen.TrainingResult.name + "/$num") {
                            popUpTo(BreatheScreen.PracticesList.name)
                        }
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
                    mainScreenButton = { navController.navigate(BreatheScreen.PracticesList.name) {
                        popUpTo(BreatheScreen.PracticesList.name)
                    } }
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
                historyButton = { navController.navigate(BreatheScreen.History.name) },
                upButton = { navController.navigateUp() }
            )
        }
        composable(BreatheScreen.History.name) {
            HistoryLayout(upButton = { navController.navigateUp() })
        }
        composable(BreatheScreen.PracticeInfo.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeInfoLayout(
                    practiceNum = it,
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BreatheTheme {
                BreatheApp()
            }
        }
    }
}
