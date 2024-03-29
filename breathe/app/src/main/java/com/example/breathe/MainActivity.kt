package com.example.breathe


import android.media.MediaPlayer
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import android.Manifest
import androidx.core.content.ContextCompat
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
    val practiceState by viewModel.practiceState.collectAsState()
    val filterState by viewModel.filterState.collectAsState()
    val settingsState by viewModel.settingsFlow.collectAsState(ProtoNotificationSettings.getDefaultInstance())
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
                        viewModel.resetPracticeState()
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
                    mainScreenButton = { navController.popBackStack() }
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
                upButton = { navController.navigateUp() },
                filter = filterState.historyFilter,
                filterChange = { viewModel.setFilter(it) }
            )
        }
        composable(BreatheScreen.PracticeInfo.name + practiceNumUrl, arguments = arguments) {
            backStackEntry ->
            backStackEntry.arguments?.getInt(practiceNumArg)?.let {
                PracticeInfoLayout(
                    practiceNum = it,
                    currentPracticeState = practiceState,
                    startButton = { num ->
                        viewModel.resetPracticeState()
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
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun isNotificationPermissionGranted() =
        ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

        createNotificationChannel(applicationContext)
        val viewModel = BreatheViewModel(
            MediaPlayer.create(this, R.raw.vot_tak_vot),
            AccelerometerHandler(this),
            DataManager(applicationContext)
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!isNotificationPermissionGranted()) {
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }
        setContent {
            BreatheTheme {
                BreatheApp(viewModel)
            }
        }
    }
}
