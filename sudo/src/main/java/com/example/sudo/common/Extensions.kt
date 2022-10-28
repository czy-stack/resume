package com.example.sudo.common

import android.app.Activity
import android.widget.Toast
import com.example.sudo.R
import com.example.sudo.domain.Difficulty

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */

internal fun Activity.makeToast(message:String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

internal fun Long.toTime():String{
    if (this >= 3600) return "+59:59"
    var minutes = ((this % 3600)/60).toString()
    if (minutes.length == 1) minutes = "0$minutes"
    var seconds = ((this % 60)).toString()
    if (seconds.length == 1) seconds = "0$seconds"
    return String.format("$minutes:$seconds")
}

internal val Difficulty.toLocalizedResource:Int
    get() {
    return when(this){
        Difficulty.EASY -> R.string.easy
        Difficulty.MEDIUM -> R.string.medium
        Difficulty.HARD -> R.string.hard
    }
}