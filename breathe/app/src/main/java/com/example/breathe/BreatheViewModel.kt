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
    val phaseTimes: IntArray = IntArray(4)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreathePracticeState

        if (totalSeconds != other.totalSeconds) return false
        if (currentSeconds != other.currentSeconds) return false
        return phaseTimes.contentEquals(other.phaseTimes)
    }

    override fun hashCode(): Int {
        var result = totalSeconds
        result = 31 * result + currentSeconds
        result = 31 * result + phaseTimes.contentHashCode()
        return result
    }
}

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

    fun setSettingsState(seconds: Int, phaseTimes: IntArray) {
        _practiceState.update { currentState ->
            currentState.copy(
                totalSeconds = seconds,
                currentSeconds = seconds,
                phaseTimes = phaseTimes
            )
        }
    }

    fun setPracticeMinutes(value: Int) {
        _practiceState.update { currentState ->
            currentState.copy(
                totalSeconds = value * 60 + currentState.totalSeconds % 60,
                currentSeconds = value * 60 + currentState.totalSeconds % 60
            )
        }
    }

    fun setPracticeSeconds(value: Int) {
        _practiceState.update { currentState ->
            currentState.copy(
                totalSeconds = value % 60 + currentState.totalSeconds - currentState.totalSeconds % 60,
                currentSeconds = value % 60 + currentState.totalSeconds - currentState.totalSeconds % 60
            )
        }
    }

    fun setPracticePhaseTime(phase: Int, time: Int) {
        fun updated( arr: IntArray, i: Int, value: Int ) : IntArray {
            val cloned = arr.clone()
            cloned[i] = value
            return cloned
        }
        _practiceState.update { currentState ->
            currentState.copy(
                phaseTimes = updated(currentState.phaseTimes, phase, time)
            )
        }
    }

    fun timerEnd() {
        // TODO save breath statistics to some storage
    }

    fun timerTick() {
        // TODO handle breathe phases with hardware
        if (_practiceState.value.currentSeconds > 0) {
            _practiceState.update { currentState ->
                currentState.copy(currentSeconds = currentState.currentSeconds - 1)
            }
        }
    }
}