package com.example.breathe

import android.util.Log
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

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

@Suppress("BlockingMethodInNonBlockingContext")
object PracticeResultListSerializer : Serializer<ProtoPracticeResultList> {
    override val defaultValue: ProtoPracticeResultList = ProtoPracticeResultList.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProtoPracticeResultList {
        return try {
            ProtoPracticeResultList.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            Log.e("PRACTICE RESULT LIST", "Cannot read proto. Create default.")
            defaultValue
        }
    }

    override suspend fun writeTo(t: ProtoPracticeResultList, output: OutputStream) = t.writeTo(output)
}

@Suppress("BlockingMethodInNonBlockingContext")
object ProfileSerializer : Serializer<ProtoProfile> {
    override val defaultValue: ProtoProfile = ProtoProfile.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProtoProfile {
        return try {
            ProtoProfile.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            Log.e("PROFILE", "Cannot read proto. Create default.")
            defaultValue
        }
    }

    override suspend fun writeTo(t: ProtoProfile, output: OutputStream) = t.writeTo(output)
}
