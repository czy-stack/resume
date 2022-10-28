package com.example.sudo.ui.activegame

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/10
 */
sealed class ActiveGameEvent {
    data class OnInput(val input:Int):ActiveGameEvent()
    data class OnTileFocused(val x:Int,val y:Int):ActiveGameEvent()
    object OnNewGameClicked:ActiveGameEvent()
    object OnStart:ActiveGameEvent()
    object OnStop:ActiveGameEvent()
}