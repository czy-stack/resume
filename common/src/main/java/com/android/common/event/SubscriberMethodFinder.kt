package com.android.common.event

import java.lang.reflect.Modifier
import java.util.concurrent.ConcurrentHashMap

/**
 * @作者 陈忠岳
 * @主要功能 扫描 @Subscribe 方法
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. 原实现只扫描 `declaredMethods`，却声称「and its super classes」，
 *    父类里声明的订阅方法会被静默漏掉；这里沿继承链向上扫描；
 * 2. 每次 register 都重新反射一遍，现将结果按 Class 缓存；
 * 3. 参数个数不对时直接报错并给出类名方法名，而不是静默跳过。
 */
object SubscriberMethodFinder {

    private val cache = ConcurrentHashMap<Class<*>, List<SubscriberMethod>>()

    fun findUsingReflection(type: Class<*>): List<SubscriberMethod> {
        return cache.getOrPut(type) { scan(type) }
    }

    /** 仅供测试使用：清空缓存 */
    internal fun clearCache() = cache.clear()

    private fun scan(type: Class<*>): List<SubscriberMethod> {
        val result = mutableListOf<SubscriberMethod>()
        var current: Class<*>? = type

        while (current != null && current != Any::class.java) {
            current.declaredMethods.forEach { method ->
                addIfSubscriber(type, method, result)
            }
            current = current.superclass
        }

        if (result.isEmpty()) {
            throw RxBusException(
                "Subscriber ${type.name} 及其父类中没有带 @Subscribe 注解的方法"
            )
        }
        return result
    }

    private fun addIfSubscriber(
        type: Class<*>,
        method: java.lang.reflect.Method,
        result: MutableList<SubscriberMethod>
    ) {
        if (method.isBridge || method.isSynthetic) return

        val subscribe = method.getAnnotation(Subscribe::class.java) ?: return
        val parameterTypes = method.parameterTypes

        if (parameterTypes.size != 1) {
            throw RxBusException(
                "@Subscribe 方法必须且只能有一个参数：${type.name}#${method.name}"
            )
        }
        if (Modifier.isStatic(method.modifiers) || Modifier.isAbstract(method.modifiers)) {
            throw RxBusException(
                "@Subscribe 方法不能是 static 或 abstract：${type.name}#${method.name}"
            )
        }

        method.isAccessible = true
        result.add(SubscriberMethod(subscribe.threadMode, parameterTypes[0], method))
    }
}
