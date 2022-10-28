package com.example.sudo.ui.newgame.buildlogic

import android.content.Context
import com.example.sudo.common.ProductionDispatcherProvider
import com.example.sudo.persitence.*
import com.example.sudo.persitence.settingsDataStore
import com.example.sudo.ui.newgame.NewGameContainer
import com.example.sudo.ui.newgame.NewGameLogic
import com.example.sudo.ui.newgame.NewGameViewModel

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
internal fun buildNewGameLogic(container: NewGameContainer,
    viewModel: NewGameViewModel,
    context: Context) : NewGameLogic{
    return NewGameLogic(
        container,
        viewModel,
        GameRepositoryImpl(LocalGameStorageImpl(context.filesDir.path),LocalSettingsStorageImpl(context.settingsDataStore)),
        LocalStatisticsStorageImpl(context.statsDataStore),
        ProductionDispatcherProvider
    )
}