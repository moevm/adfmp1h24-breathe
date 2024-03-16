package com.example.breathe

import android.content.Context
import androidx.datastore.dataStore
import com.google.android.datatransport.runtime.dagger.Module
import com.google.protobuf.Timestamp
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.time.LocalDateTime
import java.time.ZoneOffset

@Module
@InstallIn(SingletonComponent::class)
class DataManager(val context: Context) {

    private val Context.settingsDataStore by dataStore(
        fileName = "settings.pb",
        serializer = NotificationSettingsSerializer
    )
    private val Context.practicesDataStore by dataStore(
        fileName = "practices.pb",
        serializer = PracticeListSerializer
    )
    private val Context.practiceResultDataStore by dataStore(
        fileName = "practices_result.pb",
        serializer = PracticeResultListSerializer
    )

    suspend fun setEnabled(value: Boolean) = context.settingsDataStore.updateData {
        it.toBuilder().setEnabled(value).build()
    }
    suspend fun setTimeHours(value: Int) = context.settingsDataStore.updateData {
        it.toBuilder().setTimeHours(value).build()
    }
    suspend fun setTimeMinutes(value: Int) = context.settingsDataStore.updateData {
        it.toBuilder().setTimeMinutes(value).build()
    }

    suspend fun addPracticeResult(
        id: Int,
        seconds: Int,
        phaseTimes: IntArray
    ) = context.practiceResultDataStore.updateData {
        it.toBuilder().addResults(
            ProtoPracticeResult.newBuilder()
                .setId(id)
                .setTotal(seconds)
                .setEndDate(Timestamp.newBuilder()
                    .setSeconds(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                )
                .addAllPhaseTimes(phaseTimes.toList())
                .build()
        ).build()
    }

    fun getSettings()           = context.settingsDataStore.data
    fun getPractices()          = context.practicesDataStore.data
    fun getPracticeResults()    = context.practiceResultDataStore.data

}
