package com.android.common.event

import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.Disposable



/**
 * @作者 陈忠岳
 * @主要功能  背压rxBus,仿eventBus用法
 * @创建日期  2019-11-21
 */
class RxBus {
    private val mRelay: Relay<Any> = PublishRelay.create()
    private val mDisposableMap: MutableMap<Class<*>, MutableMap<Class<*>, Disposable>> = mutableMapOf()

    companion object {
        val instance: RxBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RxBus()
        }
    }

    fun register(targetClass: Class<*>) {
        for (subscriberMethod in SubscriberMethodFinder.findUsingReflection(targetClass)) {
            addSubscriber(targetClass, subscriberMethod)
        }
    }

    private fun addSubscriber(targetClass: Class<*>, subscriberMethod: SubscriberMethod) {
        synchronized(RxBus::class.java) {
            val eventClass: Class<*> = subscriberMethod.eventClass
            val disposable: Disposable = mRelay.ofType(eventClass).observeOn(subscriberMethod.threadMode).subscribe {
                subscriberMethod.method.invoke(targetClass.newInstance(), it)
            }
            val map: MutableMap<Class<*>, Disposable>
            if (mDisposableMap[targetClass] == null) {
                map = mutableMapOf()
                mDisposableMap[targetClass] = map
                map.put(eventClass, disposable)
            } else {
                map = mDisposableMap[targetClass]!!
                map.put(eventClass, disposable)
            }
        }
    }

    fun post(obj: Any) {
        synchronized(RxBus::class.java) {
            if (mRelay.hasObservers()) mRelay.accept(obj)
        }
    }

    fun unregister(targetClass: Class<*>) {
        synchronized(RxBus::class.java) {
            mDisposableMap[targetClass].let {
                if (it != null) {
                    val map: MutableMap<Class<*>, Disposable> = it
                    for ((_, value) in map) {
                        value.dispose()
                    }
                    mDisposableMap.remove(targetClass)
                } else {
                    throw RxBusException("${targetClass::class.java} haven't registered RxBus")
                }
            }
        }
    }

    fun unregister(targetClass: Class<*>, eventClass: Class<*>) {
        synchronized(RxBus::class.java) {
            mDisposableMap[targetClass].let {
                if (it != null) {
                    it[eventClass]?.dispose()
                    it.remove(eventClass)
                } else {
                    throw RxBusException("${targetClass::class.java} haven't registered RxBus")
                }
            }
        }
    }
}
