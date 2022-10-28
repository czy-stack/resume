package com.example.sudo.ui.newgame

import com.example.sudo.common.BaseLogic
import com.example.sudo.common.DispatcherProvider
import com.example.sudo.domain.IGameRepository
import com.example.sudo.domain.IStatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
class NewGameLogic(
    private val container: NewGameContainer?,
    private val viewModel: NewGameViewModel,
    private val gameRepo: IGameRepository,
    private val statsRepo: IStatisticsRepository,
    private val dispatcher: DispatcherProvider
) : BaseLogic<NewGameEvent>(), CoroutineScope {
    init {
        jobTracker = Job()
    }

    override val coroutineContext: CoroutineContext
        get() = dispatcher.provideUIContext() + jobTracker

    override fun onEvent(event: NewGameEvent) {
        when (event) {
            is NewGameEvent.OnStart -> onStart()
            is NewGameEvent.OnDonePressed -> {
                viewModel.loadingState = true
                onDonePressed()
            }
            is NewGameEvent.OnSizeChanged -> viewModel.settingState =
                viewModel.settingState.copy(boundary = event.boundary)
            is NewGameEvent.OnDifficultyChanged -> viewModel.settingState =
                viewModel.settingState.copy(difficulty = event.diff)
        }
    }

    private fun onDonePressed() {
        launch {
            gameRepo.updateSettings(viewModel.settingState,
                { createNewGame(viewModel.settingState.boundary) }, {
                    container?.showError()
                })
        }
    }

    private fun onStart() {
        launch {
            gameRepo.getSettings({
                viewModel.settingState = it
                getStatistics()
            },
                {
                    jobTracker.cancel()
                    container?.showError()
                    container?.onDoneClick()
                }
            )
        }
    }

    private fun createNewGame(boundary: Int) = launch {
        gameRepo.createNewGame(viewModel.settingState, {
            jobTracker.cancel()
            container?.onDoneClick()
        },
            {
                container?.showError()
                jobTracker.cancel()
                container?.onDoneClick()
            })
    }

    private fun getStatistics() {
        launch {
            statsRepo.getStatistics({
                viewModel.statisticsStatic = it
                viewModel.loadingState = false
            },
                {
                    container?.showError()
                }
            )
        }
    }


}