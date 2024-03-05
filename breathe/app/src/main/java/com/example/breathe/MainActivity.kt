package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.breathe.ui.theme.BreatheTheme

enum class BreatheScreen {
    PracticesList,
    Practice,
    Training,
    Result,
    Settings,
    Profile,
    History,
    PracticeInfo
}

@Composable
fun BreatheApp() {
    NavHost(
        navController = rememberNavController(),
        startDestination = BreatheScreen.PracticesList.name,
        modifier = Modifier.fillMaxSize()
    ) {
        val practiceNumArg = "practiceNum"
        val practiceNumUrl = "/{$practiceNumArg}"
        composable(BreatheScreen.PracticesList.name) {
            PracticesListLayout()
        }
        composable(BreatheScreen.Practice.name + practiceNumUrl) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeLayout(it)
            }
        }
        composable(BreatheScreen.Training.name + practiceNumUrl) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                TrainingLayout(it)
            }
        }
        composable(BreatheScreen.Result.name) {
            PracticesListLayout()
        }
        composable(BreatheScreen.Settings.name) {
            SettingsLayout()
        }
        composable(BreatheScreen.Profile.name) {
            PracticesListLayout()
        }
        composable(BreatheScreen.History.name) {
            HistoryLayout()
        }
        composable(BreatheScreen.PracticeInfo.name + practiceNumUrl) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeInfoLayout(it)
            }
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
