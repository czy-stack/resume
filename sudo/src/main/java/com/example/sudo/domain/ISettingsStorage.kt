package com.example.sudo.domain

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
interface ISettingsStorage {
    suspend fun getSettings():SettingsStorageResult
    suspend fun updateSettings(settings: Settings) : SettingsStorageResult
}
sealed class SettingsStorageResult{
    data class OnSuccess(val settings:Settings) :SettingsStorageResult()
    object OnComplete : SettingsStorageResult()
    data class OnError(val exception: Exception):SettingsStorageResult()
}