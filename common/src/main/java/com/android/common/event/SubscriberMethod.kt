package com.android.common.event

import io.reactivex.Scheduler
import java.lang.reflect.Method

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
data class SubscriberMethod (val threadMode: Scheduler, val eventClass: Class<*>, val method: Method)