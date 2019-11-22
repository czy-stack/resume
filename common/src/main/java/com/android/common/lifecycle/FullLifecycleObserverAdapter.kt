package com.android.common.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class FullLifecycleObserverAdapter(
    private val mLifecycleOwner: LifecycleOwner,
    private val mObserver: FullLifecycleObserver
) :
    LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        mObserver.onCreate(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        mObserver.onStart(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        mObserver.onResume(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        mObserver.onPause(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        mObserver.onStop(mLifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        mObserver.onDestroy(mLifecycleOwner)
    }
}