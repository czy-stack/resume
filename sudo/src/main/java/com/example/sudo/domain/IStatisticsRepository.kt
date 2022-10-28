package com.example.sudo.domain

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
interface IStatisticsRepository {
    suspend fun getStatistics(
        onSuccess : (UserStatistics) ->Unit,
        onError: (Exception) -> Unit
    )
    suspend fun updateStatistic(
        time :Long,
        diff:Difficulty,
        boundary:Int,
        onSuccess: (isRecord:Boolean) -> Unit,
        onError: (Exception) -> Unit
    )
}