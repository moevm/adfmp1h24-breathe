package com.example.breathe

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
import kotlin.math.abs

data class BreathePracticeState(
    val waitSeconds: Int = 5,
    val totalSeconds: Int = 0,
    val currentSeconds: Int = 0,
    val phaseTimes: IntArray = IntArray(4),
    val currentPhaseTimes: IntArray = IntArray(4)
) {
    override fun hashCode(): Int {
        var result = waitSeconds
        result = 31 * result + totalSeconds
        result = 31 * result + currentSeconds
        result = 31 * result + phaseTimes.contentHashCode()
        result = 31 * result + currentPhaseTimes.contentHashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BreathePracticeState

        if (waitSeconds != other.waitSeconds) return false
        if (totalSeconds != other.totalSeconds) return false
        if (currentSeconds != other.currentSeconds) return false
        if (!phaseTimes.contentEquals(other.phaseTimes)) return false
        return currentPhaseTimes.contentEquals(other.currentPhaseTimes)
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
    private val accelerometer: AccelerometerHandler,
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

    private var defaultAccelerationZ : Float = 0.0f
    private var currPhaseNum: Int = 0
    private var phaseRepeats: IntArray= IntArray(4)
    private var currPhaseTimes: IntArray= IntArray(4)

    init {
        reset()
    }

    private fun reset() {
        accelerometer.unregister()
        defaultAccelerationZ = 0.0f
        currPhaseNum = 0
        phaseRepeats = IntArray(4)
        currPhaseTimes = IntArray(4)
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
                waitSeconds = 5,
                totalSeconds = seconds,
                currentSeconds = seconds,
                phaseTimes = phaseTimes,
                currentPhaseTimes = IntArray(4)
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
        accelerometer.unregister()
        var modifier = 1.0f
        for (i in 0..<4) {
            if (phaseRepeats[i] > 0) {
                state.currentPhaseTimes[i] = currPhaseTimes[i] / phaseRepeats[i]
            }
            else {
                state.currentPhaseTimes[i] = 0
            }
            modifier *= 1.0f - (abs(state.currentPhaseTimes[i] - state.phaseTimes[i])
                    / state.phaseTimes[i].toFloat()).coerceIn(0.0f, 1.0f)
        }
        addPracticeResult(
            id,
            state.totalSeconds,
            state.totalSeconds - state.currentSeconds,
            state.phaseTimes,
            state.currentPhaseTimes
        )
        appendScore(
            (200 * modifier * (state.totalSeconds - state.currentSeconds) / state.totalSeconds.toFloat()).toInt()
        )
    }

    fun timerTick() {
        val currentSeconds = _practiceState.value.currentSeconds
        if (currentSeconds <= 0) {
            return
        }
        val waitSeconds = _practiceState.value.waitSeconds
        if (waitSeconds > 0) {
            _practiceState.update { currentState ->
                currentState.copy(waitSeconds = waitSeconds - 1)
            }
            if (waitSeconds == 1) {     // перед самим стартом упражнения
                accelerometer.unregister()
                accelerometer.register()
                currPhaseNum = 0
                defaultAccelerationZ = accelerometer.data
                phaseRepeats = IntArray(4)
                currPhaseTimes = IntArray(4)
            }
            return
        }
        val accelerationThreshold = 1e-5f
        val newSeconds = currentSeconds - 1
        val currAccelerationZ = accelerometer.data
        val prevPhaseNum = currPhaseNum
        if (currAccelerationZ - defaultAccelerationZ > accelerationThreshold) {
            currPhaseNum = 0    // вдох
        }
        else if (currAccelerationZ - defaultAccelerationZ < -accelerationThreshold)
        {
            currPhaseNum = 2    // выдох
        }
        else if (currPhaseNum % 2 == 0) {
            currPhaseNum++    // удержание
        }

        currPhaseTimes[currPhaseNum] += 1
        if (_practiceState.value.phaseTimes[currPhaseNum] == currPhaseTimes[currPhaseNum]) {
            indicateSuccess()
        }
        if (prevPhaseNum != currPhaseNum) {
            phaseRepeats[currPhaseNum] += 1
        }

        _practiceState.update { currentState ->
            currentState.copy(currentSeconds = newSeconds)
        }
    }

    private fun indicateSuccess() {

    }
}
