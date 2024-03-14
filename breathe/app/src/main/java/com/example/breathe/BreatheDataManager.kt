package com.example.breathe

import android.content.Context
import android.util.Log
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.android.datatransport.runtime.dagger.Module
import com.google.protobuf.InvalidProtocolBufferException
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.io.InputStream
import java.io.OutputStream

@Module
@InstallIn(SingletonComponent::class)
class SettingsDataManager(val context: Context) {

    @Suppress("BlockingMethodInNonBlockingContext")
    object NotificationSettingsSerializer : Serializer<ProtoNotificationSettings> {
        override val defaultValue: ProtoNotificationSettings = ProtoNotificationSettings.getDefaultInstance()

        override suspend fun readFrom(input: InputStream): ProtoNotificationSettings {
            return try {
                ProtoNotificationSettings.parseFrom(input)
            } catch (exception: InvalidProtocolBufferException) {
                Log.e("NOTIFICATION SETTINGS", "Cannot read proto. Create default.")
                defaultValue
            }
        }

        override suspend fun writeTo(t: ProtoNotificationSettings, output: OutputStream) = t.writeTo(output)
    }

    private val Context.settingsDataStore by dataStore(
        fileName = "settings.pb",
        serializer = NotificationSettingsSerializer
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

    fun getSettings() = context.settingsDataStore.data

}
