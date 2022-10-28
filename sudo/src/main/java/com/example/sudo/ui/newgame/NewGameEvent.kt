package com.example.sudo.ui.newgame

import com.example.sudo.domain.Difficulty

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
sealed class NewGameEvent {
    object OnStart:NewGameEvent()
    data class OnSizeChanged(val boundary:Int):NewGameEvent()
    data class OnDifficultyChanged(val diff:Difficulty):NewGameEvent()
    object OnDonePressed:NewGameEvent()
}