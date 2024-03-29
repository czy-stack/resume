package com.example.sudo.domain

import android.graphics.Color

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
interface IGameRepository {

    suspend fun saveGame(
        elapsedTime: Long,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    )

    suspend fun updateGame(
        game: SudokuPuzzle,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    )

    /**
     * Here we care if the puzzle is complete or not, along with if the update is successful
     */
    suspend fun updateNode(
        x: Int,
        y: Int,
        color: Int,
        elapsedTime: Long,
        onSuccess: (isComplete: Boolean) -> Unit, onError: (Exception) -> Unit
    )

    suspend fun getCurrentGame(
        onSuccess: (currentGame: SudokuPuzzle, isComplete: Boolean) -> Unit,
        onError: (Exception) -> Unit
    )

    suspend fun createNewGame(
        settings: Settings,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    )

    suspend fun getSettings(onSuccess: (Settings) -> Unit, onError: (Exception) -> Unit)
    suspend fun updateSettings(
        settings: Settings,
        onSuccess: (Unit) -> Unit,
        onError: (Exception) -> Unit
    )
}