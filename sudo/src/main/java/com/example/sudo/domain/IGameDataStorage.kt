package com.example.sudo.domain

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
interface IGameDataStorage{
    suspend fun updateGame(game:SudokuPuzzle):GameStorageResult
    suspend fun updateNode(x: Int, y: Int, color: Int, elapsedTime: Long): GameStorageResult
    suspend fun getCurrentGame():GameStorageResult
}

sealed class GameStorageResult{
    data class OnSuccess(val currentGame:SudokuPuzzle) :GameStorageResult()
    data class OnError(val exception: java.lang.Exception):GameStorageResult()
}
