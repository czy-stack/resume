package com.example.sudo.persitence

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import com.example.sudo.GameSettings
import com.example.sudo.Statistics

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/28
 */
internal val Context.settingsDataStore: DataStore<GameSettings> by dataStore(
    fileName = "game_settings.pb",
    serializer = GameSettingsSerializer
)

private object GameSettingsSerializer : Serializer<GameSettings> {
    override val defaultValue: GameSettings = GameSettings.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GameSettings {
        try {
            return GameSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: GameSettings, output: OutputStream) = t.writeTo(output)
}

internal val Context.statsDataStore: DataStore<Statistics> by dataStore(
    fileName = "user_statistics.pb",
    serializer = StatisticsSerializer
)


private object StatisticsSerializer : Serializer<Statistics> {
    override val defaultValue: Statistics = Statistics.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Statistics {
        try {
            return Statistics.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Statistics, output: OutputStream) = t.writeTo(output)
}
