package com.example.breathe

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class BreatheSettingsState(
    val notifications: Boolean = false,
    val notifyTimeHours: Int = 24,
    val notifyTimeMinutes: Int = 0,
)

data class BreathePracticeState(
    val totalSeconds: Int = 0,
    val currentSeconds: Int = 0,
    val phaseTimes: Array<Int> = arrayOf(0, 0, 0, 0)
)

class BreatheViewModel : ViewModel() {
    private val _settingsState = MutableStateFlow(BreatheSettingsState())
    private val _practiceState = MutableStateFlow(BreathePracticeState())

    val settingsState: StateFlow<BreatheSettingsState> = _settingsState.asStateFlow()
    val practiceState: StateFlow<BreathePracticeState> = _practiceState.asStateFlow()
    init {
        reset()
    }

    private fun reset() {
        _settingsState.value = BreatheSettingsState() // TODO read from some storage
        _practiceState.value = BreathePracticeState()
    }

    fun saveNotifications(value: Boolean) {
        _settingsState.update { currentState -> currentState.copy(notifications = value) }
        // TODO save to some storage
    }

    fun saveNotifyTimeHours(value: Int) {
        _settingsState.update { currentState -> currentState.copy(notifyTimeHours = value) }
        // TODO save to some storage
    }

    fun saveNotifyTimeMinutes(value: Int) {
        _settingsState.update { currentState -> currentState.copy(notifyTimeMinutes = value) }
        // TODO save to some storage
    }
}