package com.android.common.event

/**
 * @作者 陈忠岳
 * @主要功能 RxBus 的 Kotlin 扩展
 * @创建日期 2026-10-24
 *
 * 与 EventBus 的 `register()` / `unregister()` 刻意区分命名，
 * 避免两套事件机制混用时调用错误。
 */

fun Any.rxRegister() = RxBus.instance.register(this)

fun Any.rxUnregister() = RxBus.instance.unregister(this)

fun Event.postEvent() = RxBus.instance.post(this)
