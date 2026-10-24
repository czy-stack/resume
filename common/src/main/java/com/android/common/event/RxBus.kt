package com.android.common.event

import androidx.annotation.VisibleForTesting
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference
import java.util.WeakHashMap

/**
 * @作者 陈忠岳
 * @主要功能 基于 RxJava 的注解式事件总线，用法对齐 EventBus
 * @创建日期  2019-11-21
 *
 * 设计要点与早期实现的差异（原实现存在三处致命问题）：
 *
 * 1. **回调对象错误**：原实现 `register(targetClass: Class<*>)` 只能拿到 Class，
 *    派发时用 `targetClass.newInstance()` 创建了一个**全新实例**再反射调用，
 *    事件永远到不了真正的订阅者。现在改为 `register(subscriber: Any)` 持有实例引用。
 *
 * 2. **无法反注册**：只按 Class 记录，同类多实例会互相覆盖。现在以实例为 key，
 *    支持同一类注册多个对象，并可按实例精确反注册。
 *
 * 3. **内存泄漏**：订阅者被事务性强引用。现在用 [WeakReference] 持有订阅者，
 *    Disposable 侧不强引用 key，配合 [unregister] 可确保页面销毁后不再回调。
 *
 * 线程模型：事件默认在 [ThreadMode.MAIN] 派发，通过构造参数注入主线程调度器，
 * 单元测试可传入 `Schedulers.trampoline()` 或 TestScheduler，无需 Android Looper。
 */
class RxBus @VisibleForTesting internal constructor(
    private val mainScheduler: Scheduler
) {

    companion object {
        val instance: RxBus by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RxBus(AndroidSchedulers.mainThread())
        }
    }

    private val relay: Relay<Any> = PublishRelay.create()

    /** key 为订阅者实例，value 为该实例下所有订阅的 Disposable */
    private val registry = WeakHashMap<Any, MutableList<Disposable>>()

    /**
     * 注册订阅者。重复注册会先反注册旧订阅，避免同一实例收到多次事件。
     *
     * @throws RxBusException 订阅者及其父类没有任何 @Subscribe 方法
     */
    @Synchronized
    fun register(subscriber: Any) {
        unregister(subscriber)

        // 用弱引用包一层：Disposable 的闭包不能强引用 subscriber，
        // 否则 WeakHashMap 的 value 会反过来钉住 key，弱引用形同虚设。
        val ref = WeakReference(subscriber)
        val methods = SubscriberMethodFinder.findUsingReflection(subscriber.javaClass)

        val disposables = methods.map { method ->
            relay.ofType(method.eventClass)
                .observeOn(schedulerOf(method.threadMode))
                .subscribe { event -> dispatch(ref, method, event) }
        }

        registry[subscriber] = disposables.toMutableList()
    }

    /**
     * 反注册，可重复调用。
     */
    @Synchronized
    fun unregister(subscriber: Any) {
        registry.remove(subscriber)?.forEach { it.dispose() }
    }

    /**
     * 发送事件。没有订阅者时直接返回，不产生额外对象。
     */
    fun post(event: Any) {
        if (relay.hasObservers()) {
            relay.accept(event)
        }
    }

    /** 当前是否存在订阅者 */
    fun hasSubscribers(): Boolean = relay.hasObservers()

    /** 清空所有订阅，仅建议在测试或进程退出时使用 */
    @Synchronized
    fun clear() {
        registry.values.flatten().forEach { it.dispose() }
        registry.clear()
    }

    private fun dispatch(ref: WeakReference<Any>, method: SubscriberMethod, event: Any) {
        val subscriber = ref.get() ?: return
        runCatching {
            method.method.invoke(subscriber, event)
        }.onFailure { throwable ->
            // 反射调用抛出的异常会被包在 InvocationTargetException 里
            val cause = throwable.cause ?: throwable
            throw RxBusException(
                "事件派发失败：${subscriber.javaClass.name}#${method.method.name} -> ${cause.message}"
            )
        }
    }

    private fun schedulerOf(threadMode: ThreadMode): Scheduler = when (threadMode) {
        ThreadMode.MAIN -> mainScheduler
        ThreadMode.COMPUTATION -> Schedulers.computation()
        ThreadMode.IO -> Schedulers.io()
        ThreadMode.NEW_THREAD -> Schedulers.newThread()
        ThreadMode.SINGLE -> Schedulers.single()
        ThreadMode.TRAMPOLINE -> Schedulers.trampoline()
    }
}
