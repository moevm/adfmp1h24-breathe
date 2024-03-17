package com.example.breathe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.abs
import kotlin.random.Random

data class BreathePracticeState(
    val totalSeconds: Int = 0,
    val currentSeconds: Int = 0,
    val phaseTimes: IntArray = IntArray(4),
    val currentPhaseTimes: IntArray = IntArray(4)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreathePracticeState

        if (totalSeconds != other.totalSeconds) return false
        if (currentSeconds != other.currentSeconds) return false
        if (!phaseTimes.contentEquals(other.phaseTimes)) return false
        return currentPhaseTimes.contentEquals(other.currentPhaseTimes)
    }

    override fun hashCode(): Int {
        var result = totalSeconds
        result = 31 * result + currentSeconds
        result = 31 * result + phaseTimes.contentHashCode()
        result = 31 * result + currentPhaseTimes.contentHashCode()
        return result
    }
}

data class BreatheFilterState(
    val historyFilter: String = ""
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreatheFilterState

        return historyFilter.contentEquals(other.historyFilter)
    }

    override fun hashCode(): Int {
        return historyFilter.hashCode()
    }
}

@HiltViewModel
class BreatheViewModel @Inject constructor(
    private val dataManager: DataManager
) : ViewModel() {
    private val _practiceState  = MutableStateFlow(BreathePracticeState())
    private val _filterState    = MutableStateFlow(BreatheFilterState())
    private val _settingsFlow   = dataManager.getSettings()
    private val _resultsFlow    = dataManager.getPracticeResults()
    private val _profileFlow    = dataManager.getProfile()

    val practiceState: StateFlow<BreathePracticeState>  = _practiceState.asStateFlow()
    val filterState: StateFlow<BreatheFilterState>      = _filterState.asStateFlow()
    val settingsFlow: Flow<ProtoNotificationSettings>   = _settingsFlow
    val resultsFlow: Flow<ProtoPracticeResultList>      = _resultsFlow
    val profileFlow: Flow<ProtoProfile>                 = _profileFlow
    init {
        reset()
    }

    private fun reset() {
        _practiceState.value = BreathePracticeState()
        _filterState.value = BreatheFilterState()
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
                phaseTimes = phaseTimes,
                currentPhaseTimes = listOf(
                    Random.nextInt(0, 4),
                    Random.nextInt(0, 4),
                    Random.nextInt(3, 12),
                    Random.nextInt(3, 12)
                ).toIntArray() // TODO
            )
        }
    }

    fun setFilter(filter: String) {
        _filterState.update { currentState ->
            currentState.copy(historyFilter = filter)
        }
    }

    fun addPracticeResult(
        id: Int,
        seconds: Int,
        resSeconds: Int,
        phaseTimes: IntArray,
        resPhaseTimes: IntArray
    ) = viewModelScope.launch {
        dataManager.addPracticeResult(id, seconds, resSeconds, phaseTimes, resPhaseTimes)
        updateUsage()
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

    private fun updateUsage() = viewModelScope.launch {
        val secondsInDay: Long = 60 * 60 * 24
        val currentUsage = (System.currentTimeMillis() / 1000)

        var daysUsage = 0
        profileFlow.collect { daysUsage = it.daysUsingInRow }

        var lastUsage: Long = 0
        profileFlow.collect { lastUsage = it.lastUsage.seconds }

        /// Вход на следующий день
        if ((lastUsage / secondsInDay) == (currentUsage / secondsInDay - 1)) {
            profileFlow.collect { daysUsage += 1 }
            dataManager.appendScore(100)
        }
        dataManager.setUsage( daysUsage, currentUsage )
    }

    private fun appendScore(appendValue: Int) = viewModelScope.launch {
        dataManager.appendScore(appendValue)
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
            state.totalSeconds,
            state.totalSeconds - state.currentSeconds,
            state.phaseTimes,
            state.currentPhaseTimes
        )
        var modifier = 1.0f
        for (i in 0..<4) {
             modifier *= 1.0f - (abs(state.currentPhaseTimes[i] - state.phaseTimes[i])
                    / state.phaseTimes[i].toFloat()).coerceIn(0.0f, 1.0f)
        }
        appendScore(
            (200 * modifier * (state.totalSeconds - state.currentSeconds) / state.totalSeconds.toFloat()).toInt()
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