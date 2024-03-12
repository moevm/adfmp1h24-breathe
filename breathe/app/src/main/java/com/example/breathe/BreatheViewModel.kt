package com.example.breathe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BreatheSettingsState(
    val notifications: Boolean = false,
    val notifyTimeHours: Int = 24,
    val notifyTimeMinutes: Int = 0,
)

class BreatheViewModel : ViewModel() {
    private val _settingsState = MutableStateFlow(BreatheSettingsState())
    val settingsState: StateFlow<BreatheSettingsState> = _settingsState.asStateFlow()

    init {
        reset()
    }

    private fun reset() {
        _settingsState.value = BreatheSettingsState() // TODO read from some storage
    }

}