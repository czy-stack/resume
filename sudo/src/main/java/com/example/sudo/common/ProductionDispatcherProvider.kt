package com.example.sudo.common

import kotlin.coroutines.CoroutineContext

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
object ProductionDispatcherProvider : DispatcherProvider {
    override fun provideUIContext(): CoroutineContext {
        return kotlinx.coroutines.Dispatchers.Main
    }

    override fun provideIOContext(): CoroutineContext {
        return kotlinx.coroutines.Dispatchers.IO
    }
}