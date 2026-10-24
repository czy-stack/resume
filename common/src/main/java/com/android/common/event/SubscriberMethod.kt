package com.android.common.event

import java.lang.reflect.Method

/**
 * @作者 陈忠岳
 * @主要功能 一个 @Subscribe 方法的描述
 * @创建日期  2019-11-21
 *
 * 注意：[threadMode] 是项目自定义的线程枚举，具体调度器由 [RxBus] 解析，
 * 这样单元测试可以注入 TestScheduler / TrampolineScheduler，而不必依赖 Android 主线程。
 */
data class SubscriberMethod(
    val threadMode: ThreadMode,
    val eventClass: Class<*>,
    val method: Method
)
