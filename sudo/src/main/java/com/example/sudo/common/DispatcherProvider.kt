package com.example.sudo.common

import kotlin.coroutines.CoroutineContext

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/12
 */
interface DispatcherProvider {
    fun provideUIContext(): CoroutineContext
    fun provideIOContext(): CoroutineContext
}
