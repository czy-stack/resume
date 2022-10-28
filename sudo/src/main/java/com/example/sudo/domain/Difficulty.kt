package com.example.sudo.domain

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
enum class Difficulty(val modifier:Double) {
    EASY(0.50),
    MEDIUM(0.44),
    HARD(0.38)
}