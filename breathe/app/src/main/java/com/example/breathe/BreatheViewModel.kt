package com.example.breathe

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

    private val _resources = dataManager.context.resources
    private val _achievementNames = _resources.getStringArray(R.array.achievements_list)
    private val _achievementsCondition = mapOf(
        _achievementNames[0] to 1,
        _achievementNames[1] to 7,
        _achievementNames[2] to 14,
        _achievementNames[3] to 21,
        _achievementNames[4] to 30,
        _achievementNames[5] to 6 * 30,
        _achievementNames[6] to 365
    )

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

    private fun addPracticeResult(
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

    private fun checkAchievements(days: Int) = viewModelScope.launch {
        val achievementsList = mutableListOf<ProtoAchievement>()
        for (achievement in _resources.getStringArray(R.array.achievements_list)) {
            val protoAchievement = profileFlow.first().achievementsList.find {
                it.name == achievement
            }
            achievementsList.add(
                ProtoAchievement
                    .getDefaultInstance()
                    .toBuilder()
                    .setName(achievement)
                    .setActive(
                        (protoAchievement?.active ?: false)
                        || days >= (_achievementsCondition[achievement] ?: 0)
                    )
                    .build()
            )
        }
        dataManager.setAchievements(achievementsList)
    }

    private fun updateUsage() = viewModelScope.launch {
        val secondsInDay: Long = 60 * 60 * 24
        val currentUsage = (System.currentTimeMillis() / 1000)

        val data = profileFlow.first()
        var daysUsage = data.daysUsingInRow
        val lastUsage = data.lastUsage.seconds
        val daysDiff = (currentUsage / secondsInDay) - (lastUsage / secondsInDay)

        /// Вход на следующий день
        if (daysDiff == 1.toLong()) {
            daysUsage += 1
            dataManager.appendScore(100)
        } else if (daysDiff > 1.toLong()) {
            daysUsage = 0
        }
        dataManager.setUsage(daysUsage, currentUsage)
        checkAchievements(daysUsage)
        Log.d("achievement", "updating")
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