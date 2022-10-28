package com.example.sudo.persitence

import androidx.datastore.core.DataStore
import com.example.sudo.Statistics
import com.example.sudo.domain.Difficulty
import com.example.sudo.domain.IStatisticsRepository
import com.example.sudo.domain.UserStatistics
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/28
 */

class LocalStatisticsStorageImpl(
    private val dataStore: DataStore<Statistics>
) : IStatisticsRepository {
    override suspend fun getStatistics(
        onSuccess: (UserStatistics) -> Unit,
        onError: (Exception) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val stats = dataStore.data.first()

            onSuccess(
                stats.toUserStatistics
            )
        } catch (e: Exception) {
            onError(e)
        }
    }

    override suspend fun updateStatistic(
        time: Long,
        diff: Difficulty,
        boundary: Int,
        onSuccess: (isRecord: Boolean) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val stats = dataStore.data.first()

            val oldTime = stats.findMatch(diff, boundary)

            if (oldTime > time || oldTime == 0L) {
                val userStats = stats.toUserStatistics.updateMatch(time, diff, boundary)
                dataStore.updateData { stats ->
                    stats.toBuilder()
                        .setFourEasy(userStats.fourEasy)
                        .setFourMedium(userStats.fourMedium)
                        .setFourHard(userStats.fourHard)
                        .setNineEasy(userStats.nineEasy)
                        .setNineMedium(userStats.nineMedium)
                        .setNineHard(userStats.nineHard)
                        .build()
                }

                onSuccess(true)
            } else {
                onSuccess(false)
            }

        } catch (e: Exception) {
            onError(e)
        }
    }

    private val Statistics.toUserStatistics: UserStatistics
        get() {
            return UserStatistics(
                this.fourEasy,
                this.fourMedium,
                this.fourHard,
                this.nineEasy,
                this.nineMedium,
                this.nineHard
            )
        }

    private fun Statistics.findMatch(diff: Difficulty, boundary: Int): Long {
        return when {
            diff == Difficulty.EASY && boundary == 4 -> fourEasy
            diff == Difficulty.MEDIUM && boundary == 4 -> fourMedium
            diff == Difficulty.HARD && boundary == 4 -> fourHard

            diff == Difficulty.EASY && boundary == 9 -> nineEasy
            diff == Difficulty.MEDIUM && boundary == 9 -> nineMedium
            diff == Difficulty.HARD && boundary == 9 -> nineHard

            else -> throw IOException()
        }
    }

    private fun UserStatistics.updateMatch(
        time: Long,
        diff: Difficulty,
        boundary: Int
    ): UserStatistics {
        return when {
            diff == Difficulty.EASY && boundary == 4 -> this.copy(fourEasy = time)
            diff == Difficulty.MEDIUM && boundary == 4 -> this.copy(fourMedium = time)
            diff == Difficulty.HARD && boundary == 4 -> this.copy(fourHard = time)

            diff == Difficulty.EASY && boundary == 9 -> this.copy(nineEasy = time)
            diff == Difficulty.MEDIUM && boundary == 9 -> this.copy(nineMedium = time)
            diff == Difficulty.HARD && boundary == 9 -> this.copy(nineHard = time)

            else -> {
                throw IOException()
            }
        }
    }
}