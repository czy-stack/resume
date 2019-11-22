package com.android.common.lifecycle

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner



/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface FullLifecycleObserver :LifecycleObserver {
    fun onCreate(owner: LifecycleOwner)

    fun onStart(owner: LifecycleOwner)

    fun onResume(owner: LifecycleOwner)

    fun onPause(owner: LifecycleOwner)

    fun onStop(owner: LifecycleOwner)

    fun onDestroy(owner: LifecycleOwner)
}