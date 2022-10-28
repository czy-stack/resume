package com.example.sudo.common

import kotlinx.coroutines.Job

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
abstract class BaseLogic<EVENT> {
    protected lateinit var jobTracker: Job
    abstract fun onEvent(event: EVENT)
}
