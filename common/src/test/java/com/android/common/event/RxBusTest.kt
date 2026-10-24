package com.android.common.event

import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * RxBus 行为测试。
 *
 * 这些用例正是为了锁住原实现的三个缺陷：
 * 1. 事件必须到达「注册的那个实例」，而不是反射新建的对象；
 * 2. 反注册后必须收不到；
 * 3. 同一类的多个实例之间不能串台。
 */
class RxBusTest {

    private lateinit var bus: RxBus

    @Before
    fun setUp() {
        SubscriberMethodFinder.clearCache()
        // 用 trampoline 替代 Android 主线程调度器，测试可在纯 JVM 运行
        bus = RxBus(Schedulers.trampoline())
    }

    @After
    fun tearDown() {
        bus.clear()
    }

    // ---------- 测试用事件与订阅者 ----------

    class EventA(val value: String)
    class EventB(val value: String)

    class Collector {
        val received = mutableListOf<Any>()

        @Subscribe
        fun onEventA(event: EventA) {
            received += event
        }

        @Subscribe
        fun onEventB(event: EventB) {
            received += event
        }
    }

    open class BaseCollector {
        val baseReceived = mutableListOf<EventA>()

        @Subscribe
        fun onBaseEvent(event: EventA) {
            baseReceived += event
        }
    }

    class DerivedCollector : BaseCollector() {
        val derivedReceived = mutableListOf<EventA>()

        @Subscribe
        fun onDerivedEvent(event: EventA) {
            derivedReceived += event
        }
    }

    class NotASubscriber

    // ---------- 用例 ----------

    @Test
    fun `post 后注册的实例能收到事件`() {
        val collector = Collector()
        bus.register(collector)

        bus.post(EventA("hello"))

        assertEquals(1, collector.received.size)
        assertTrue(collector.received[0] is EventA)
        assertEquals("hello", (collector.received[0] as EventA).value)
    }

    @Test
    fun `事件只投递给参数类型匹配的方法`() {
        val collector = Collector()
        bus.register(collector)

        bus.post(EventB("only-b"))

        assertEquals(1, collector.received.size)
        assertTrue(collector.received[0] is EventB)
    }

    @Test
    fun `反注册后不再收到事件`() {
        val collector = Collector()
        bus.register(collector)
        bus.post(EventA("first"))
        assertEquals(1, collector.received.size)

        bus.unregister(collector)
        bus.post(EventA("second"))

        assertEquals("反注册后不应再累积事件", 1, collector.received.size)
    }

    @Test
    fun `同一类的两个实例互不干扰`() {
        val first = Collector()
        val second = Collector()
        bus.register(first)
        bus.register(second)

        bus.post(EventA("broadcast"))

        assertEquals(1, first.received.size)
        assertEquals(1, second.received.size)

        bus.unregister(first)
        bus.post(EventA("after-unregister"))

        assertEquals("已反注册的实例不应再收到", 1, first.received.size)
        assertEquals("未反注册的实例应继续收到", 2, second.received.size)
    }

    @Test
    fun `父类声明的订阅方法也会被扫描到`() {
        val collector = DerivedCollector()
        bus.register(collector)

        bus.post(EventA("inherited"))

        assertEquals("父类方法应收到", 1, collector.baseReceived.size)
        assertEquals("子类方法应收到", 1, collector.derivedReceived.size)
    }

    @Test
    fun `重复注册不会造成重复派发`() {
        val collector = Collector()
        bus.register(collector)
        bus.register(collector)

        bus.post(EventA("once"))

        assertEquals("重复注册应先反注册旧订阅", 1, collector.received.size)
    }

    @Test
    fun `没有订阅方法时注册应抛异常`() {
        try {
            bus.register(NotASubscriber())
            throw AssertionError("应当抛出 RxBusException")
        } catch (expected: RxBusException) {
            assertTrue(expected.message!!.contains("Subscribe"))
        }
    }

    @Test
    fun `无订阅者时 post 不抛异常且状态正确`() {
        assertFalse(bus.hasSubscribers())
        bus.post(EventA("nobody"))
        assertFalse(bus.hasSubscribers())
    }

    @Test
    fun `反注册未注册的实例不会抛异常`() {
        bus.unregister(Collector())
    }
}
