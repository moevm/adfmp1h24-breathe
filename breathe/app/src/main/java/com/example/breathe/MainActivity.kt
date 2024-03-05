package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Result,
    Settings,
    Profile,
    History,
    PracticeInfo,
    About
}

@Composable
fun BreatheApp() {
    val navController = rememberNavController()
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
                        navController.navigate(BreatheScreen.Training.name + "/$num") },
                    upButton = { navController.navigateUp() }
                )
            }
        }
        composable(BreatheScreen.Training.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                TrainingLayout(
                    practiceNum = it,
                    stopButton = { navController.navigate(BreatheScreen.Result.name) })
            }
        }
        composable(BreatheScreen.Result.name) {
            SettingsLayout(upButton = { navController.navigateUp() })
        }
        composable(BreatheScreen.Settings.name) {
            SettingsLayout(upButton = { navController.navigateUp() })
        }
        composable(BreatheScreen.Profile.name) {
            SettingsLayout(upButton = { navController.navigateUp() })
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
            SettingsLayout(upButton = { navController.navigateUp() })
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
