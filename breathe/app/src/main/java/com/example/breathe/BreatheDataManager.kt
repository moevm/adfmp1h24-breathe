package com.example.breathe

import android.content.Context
import androidx.datastore.dataStore
import com.google.android.datatransport.runtime.dagger.Module
import com.google.protobuf.Timestamp
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import java.time.ZoneOffset

private val Context.settingsDataStore by dataStore(
    fileName = "settings.pb",
    serializer = NotificationSettingsSerializer
)
private val Context.practiceResultDataStore by dataStore(
    fileName = "practices_result.pb",
    serializer = PracticeResultListSerializer
)
private val Context.profileDataStore by dataStore(
    fileName = "profile.pb",
    serializer = ProfileSerializer
)

@Module
@InstallIn(SingletonComponent::class)
class DataManager(val context: Context) {

    suspend fun setEnabled(value: Boolean) = context.settingsDataStore.updateData {
        it.toBuilder().setEnabled(value).build()
    }
    suspend fun setTimeHours(value: Int) = context.settingsDataStore.updateData {
        it.toBuilder().setTimeHours(value).build()
    }
    suspend fun setTimeMinutes(value: Int) = context.settingsDataStore.updateData {
        it.toBuilder().setTimeMinutes(value).build()
    }

    suspend fun setAchievements(achievements: List<ProtoAchievement>) = context.profileDataStore.updateData {
        it.toBuilder().clearAchievements().addAllAchievements(achievements).build()
    }

    suspend fun appendScore(appendValue: Int) = context.profileDataStore.updateData {
        it.toBuilder().setScore(it.score + appendValue).build()
    }

    suspend fun setUsage(daysUsing: Int, lastUsage: Long) = context.profileDataStore.updateData {
        it.toBuilder().setDaysUsingInRow(daysUsing).setLastUsage(
            it.lastUsage.toBuilder().setSeconds(lastUsage)
        ).build()
    }

    suspend fun addPracticeResult(
        id: Int,
        seconds: Int,
        resSeconds: Int,
        phaseTimes: IntArray,
        resPhaseTimes: IntArray
    ) = context.practiceResultDataStore.updateData {
        it.toBuilder().addResults(
            ProtoPracticeResult.newBuilder()
                .setId(id)
                .setTotal(seconds)
                .setResTotal(resSeconds)
                .setEndDate(Timestamp.newBuilder()
                    .setSeconds(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                )
                .addAllPhaseTimes(phaseTimes.toList())
                .addAllResPhaseTimes(resPhaseTimes.toList())
                .build()
        ).build()
    }

    fun getSettings()           = context.settingsDataStore.data
    fun getPracticeResults()    = context.practiceResultDataStore.data
    fun getProfile()            = context.profileDataStore.data

}
