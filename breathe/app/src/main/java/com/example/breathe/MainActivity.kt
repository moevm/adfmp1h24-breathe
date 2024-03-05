package com.example.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.breathe.ui.theme.BreatheTheme

enum class BreatheScreen() {
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
