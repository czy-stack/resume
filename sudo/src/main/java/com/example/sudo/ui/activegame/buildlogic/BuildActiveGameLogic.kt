package com.example.sudo.ui.activegame.buildlogic

import android.content.Context
import com.example.sudo.common.ProductionDispatcherProvider
import com.example.sudo.persitence.*
import com.example.sudo.persitence.settingsDataStore
import com.example.sudo.ui.activegame.ActiveGameContainer
import com.example.sudo.ui.activegame.ActiveGameLogic
import com.example.sudo.ui.activegame.ActiveGameViewModel

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
internal fun buildActiveGameLogic(
    container: ActiveGameContainer,
    viewModel: ActiveGameViewModel,
    context: Context
): ActiveGameLogic {
    return ActiveGameLogic(
        container,
        viewModel,
        GameRepositoryImpl(
            LocalGameStorageImpl(context.filesDir.path),
            LocalSettingsStorageImpl(context.settingsDataStore)
        ),
        LocalStatisticsStorageImpl(
            context.statsDataStore
        ),
        ProductionDispatcherProvider
    )
}