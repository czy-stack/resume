package com.example.sudo.domain

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
data class UserStatistics(
    val fourEasy:Long = 0,
    val fourMedium:Long = 0,
    val fourHard:Long = 0,
    val nineEasy:Long = 0,
    val nineMedium:Long = 0,
    val nineHard:Long = 0,
)
