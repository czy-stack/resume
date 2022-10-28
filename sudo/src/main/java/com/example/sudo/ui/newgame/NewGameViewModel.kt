package com.example.sudo.ui.newgame

import com.example.sudo.domain.Settings
import com.example.sudo.domain.UserStatistics

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
class NewGameViewModel {

    internal lateinit var settingState: Settings
    internal lateinit var statisticsStatic: UserStatistics
    internal var loadingState: Boolean = true
    set(value) {
        field = value
        subLoadingState?.invoke(field)
    }
    internal var subLoadingState: ((Boolean) -> Unit)? = null
}