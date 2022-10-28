package com.example.sudo.persitence

import androidx.datastore.core.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import com.example.sudo.GameSettings
import com.example.sudo.Statistics
import com.example.sudo.domain.Difficulty
import com.example.sudo.domain.ISettingsStorage
import com.example.sudo.domain.Settings
import com.example.sudo.domain.SettingsStorageResult

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/28
 */

/**
 * Note: GameSettings is a class generated for Proto Datastore
 */
class LocalSettingsStorageImpl(
    private val dataStore: DataStore<GameSettings>
) : ISettingsStorage {
    override suspend fun getSettings(): SettingsStorageResult =
        withContext(Dispatchers.IO) {
            try {
                val gameSettings = dataStore.data.first()
                SettingsStorageResult.OnSuccess(gameSettings.toSettings)
            } catch (e: Exception) {
                SettingsStorageResult.OnError(e)
            }
        }


    override suspend fun updateSettings(settings: Settings): SettingsStorageResult =
        withContext(Dispatchers.IO) {
            try {
                dataStore.updateData { gameSettings ->
                    gameSettings.toBuilder()
                        .setBoundary(settings.boundary)
                        .setDifficulty(settings.difficulty.toProto)
                        .build()
                }

                SettingsStorageResult.OnComplete

            } catch (e: Exception) {
                SettingsStorageResult.OnError(e)
            }
        }
}

private val GameSettings.toSettings: Settings
    get() = Settings(
        this.difficulty.toDomain,
        this.boundary.verify()
    )

private val GameSettings.ProtoDifficulty.toDomain: Difficulty
    get() = when (this.number) {
        1 -> Difficulty.EASY
        2 -> Difficulty.MEDIUM
        3 -> Difficulty.HARD
        else -> Difficulty.MEDIUM
    }

private val Difficulty.toProto: GameSettings.ProtoDifficulty
    get() =  when (this) {
        Difficulty.EASY -> GameSettings.ProtoDifficulty.EASY
        Difficulty.MEDIUM -> GameSettings.ProtoDifficulty.MEDIUM
        Difficulty.HARD -> GameSettings.ProtoDifficulty.HARD
    }


private fun Int.verify(): Int {
    return when (this) {
        4 -> this
        9 -> this
        16 -> this
        else -> 4
    }
}