package com.example.breathe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class BreatheViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {
    private val _practiceState  = MutableStateFlow(BreathePracticeState())
    private val _settingsFlow   = dataManager.getSettings()
    private val _resultsFlow    = dataManager.getPracticeResults()

    val settingsFlow: Flow<ProtoNotificationSettings>   = _settingsFlow
    val practiceState: StateFlow<BreathePracticeState>  = _practiceState.asStateFlow()
    val resultsFlow: Flow<ProtoPracticeResultList>      = _resultsFlow
    init {
        reset()
    }

    private fun reset() {
        _practiceState.value = BreathePracticeState()
    }

    fun saveNotifications(value: Boolean) = viewModelScope.launch {
        dataManager.setEnabled(value)
    }

    fun saveNotifyTimeHours(value: Int) = viewModelScope.launch {
        dataManager.setTimeHours(value)
    }

    fun saveNotifyTimeMinutes(value: Int) = viewModelScope.launch {
        dataManager.setTimeMinutes(value)
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

    fun addPracticeResult(id: Int, seconds: Int, phaseTimes: IntArray) = viewModelScope.launch {
        dataManager.addPracticeResult(id, seconds, phaseTimes)
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

    fun timerEnd(id: Int, state: BreathePracticeState) {
        addPracticeResult(
            id,
            state.totalSeconds - state.currentSeconds,
            state.phaseTimes
        )
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