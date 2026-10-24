package com.example.kotlin.activity.test

import android.view.View
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.data.ShareRepository
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * 搜索页 Presenter 的行为测试。
 *
 * 覆盖四类容易被回归破坏的行为：
 * 1. 输入防抖（连续输入只查一次）；
 * 2. 相同关键词不重复查询；
 * 3. 切换市场时沿用当前关键词重查；
 * 4. 历史记录倒序、删除与清空后的视图刷新。
 *
 * 通过注入 [TestScheduler] 与内存版 [ShareRepository]，整个用例在纯 JVM 上运行，
 * 不需要 Robolectric 或真机。
 */
class TestPresenterTest {

    private lateinit var view: FakeView
    private lateinit var repository: FakeShareRepository
    private lateinit var scheduler: TestScheduler
    private lateinit var presenter: TestPresenter

    private val debounce = 800L

    @Before
    fun setUp() {
        view = FakeView()
        repository = FakeShareRepository(
            initial = listOf(
                ShareData(symbol = "1", name = "腾讯控股", describe = "00700", type = MainConstants.HK),
                ShareData(symbol = "2", name = "苹果", describe = "AAPL", type = MainConstants.USA)
            )
        )
        scheduler = TestScheduler()
        presenter = TestPresenter(
            view = view,
            lifecycleOwner = null,
            repository = repository,
            timerScheduler = scheduler,
            uiScheduler = scheduler,
            debounceMillis = debounce,
            eventPublisher = { lastEvent = it }
        )
    }

    private var lastEvent: com.android.common.event.Event? = null

    private fun advanceDebounce() {
        scheduler.advanceTimeBy(debounce + 1, TimeUnit.MILLISECONDS)
        scheduler.triggerActions()
    }

    @Test
    fun `start 展示历史记录且按新到旧排序`() {
        presenter.start()

        val call = view.lastCall()
        assertTrue(call.fromHistory)
        assertEquals(listOf("苹果", "腾讯控股"), call.list.map { it.name })
    }

    @Test
    fun `连续输入在防抖窗口内不会触发查询`() {
        presenter.onKeywordChanged("腾")
        presenter.onKeywordChanged("腾讯")
        presenter.onKeywordChanged("腾讯控")

        scheduler.advanceTimeBy(debounce - 100, TimeUnit.MILLISECONDS)

        assertEquals("防抖期间不应查询", 0, repository.searchCount)
    }

    @Test
    fun `防抖窗口结束后只查询最后一次输入`() {
        presenter.onKeywordChanged("腾")
        presenter.onKeywordChanged("腾讯")
        presenter.onKeywordChanged("腾讯控")

        advanceDebounce()

        assertEquals("连续输入只应查一次", 1, repository.searchCount)
        assertEquals("腾讯控", repository.lastKeyword)
    }

    @Test
    fun `相同关键词不会重复发起查询`() {
        presenter.onKeywordChanged("腾讯")
        advanceDebounce()
        assertEquals(1, repository.searchCount)

        presenter.onKeywordChanged("腾讯")
        advanceDebounce()

        assertEquals("相同关键词应被过滤", 1, repository.searchCount)
    }

    @Test
    fun `点击搜索立即查询且不受防抖影响`() {
        presenter.onKeywordSubmitted("腾讯")

        assertEquals("提交路径应同步查询", 1, repository.searchCount)
        assertEquals(MainConstants.HK, repository.lastType)
    }

    @Test
    fun `提交空关键词回退到历史记录`() {
        presenter.onKeywordSubmitted("")

        val call = view.lastCall()
        assertTrue(call.fromHistory)
        assertEquals(0, repository.searchCount)
    }

    @Test
    fun `切换市场时用当前关键词重查`() {
        presenter.onKeywordSubmitted("腾讯")
        assertEquals(1, repository.searchCount)
        assertEquals(MainConstants.HK, repository.lastType)

        presenter.onTypeChanged(MainConstants.USA)

        assertEquals(2, repository.searchCount)
        assertEquals(MainConstants.USA, repository.lastType)
        assertEquals("腾讯", repository.lastKeyword)
    }

    @Test
    fun `切换到已是当前市场时不做任何查询`() {
        presenter.onKeywordSubmitted("腾讯")
        presenter.onTypeChanged(MainConstants.HK)

        assertEquals(1, repository.searchCount)
    }

    @Test
    fun `清空输入框回到历史列表`() {
        presenter.onKeywordSubmitted("腾讯")
        presenter.onKeywordChanged("")

        val call = view.lastCall()
        assertTrue(call.fromHistory)
    }

    @Test
    fun `选中结果会写入历史并广播事件`() {
        presenter.start()
        val item = view.lastCall().list.first()

        presenter.onShareSelected(item)

        assertEquals(listOf(item), repository.saved)
        assertEquals(item.name, lastEvent?.message)
    }

    @Test
    fun `删除单条历史后刷新列表`() {
        presenter.start()
        val item = view.lastCall().list.first()

        presenter.onHistoryDeleted(item)

        assertEquals(listOf(item), repository.deleted)
        assertTrue(view.lastCall().fromHistory)
    }

    @Test
    fun `清空历史后展示空列表`() {
        presenter.start()

        presenter.onClearHistory()

        assertTrue(repository.cleared)
        assertTrue("清空后应回到历史模式", view.lastCall().fromHistory)
        assertTrue("清空后列表应为空", view.lastCall().list.isEmpty())
    }

    // ---------- 测试替身 ----------

    private class FakeView : TestContract.View {
        override lateinit var presenter: TestContract.Presenter

        data class Call(val list: List<ShareData>, val fromHistory: Boolean)

        private val calls = mutableListOf<Call>()

        override fun showShares(list: List<ShareData>, fromHistory: Boolean) {
            calls += Call(list, fromHistory)
        }

        override fun showToastView(message: Any) = Unit

        override fun showSnackBar(view: View, message: Any) = Unit

        override fun showLoading(message: String) = Unit

        override fun showLoading() = Unit

        override fun hiddenLoading() = Unit

        override fun finish() = Unit

        fun lastCall(): Call = calls.last()
    }

    private class FakeShareRepository(initial: List<ShareData> = emptyList()) : ShareRepository {

        private val items = initial.toMutableList()

        var searchCount = 0
            private set
        var lastKeyword: String? = null
            private set
        var lastType: Int? = null
            private set
        val saved = mutableListOf<ShareData>()
        val deleted = mutableListOf<ShareData>()
        var cleared = false
            private set

        override fun history(): List<ShareData> = items.toList()

        override fun search(type: Int, keyword: String, limit: Int): List<ShareData> {
            searchCount++
            lastKeyword = keyword
            lastType = type
            return items.filter { it.name.contains(keyword, ignoreCase = true) }.take(limit)
        }

        override fun save(item: ShareData): Boolean {
            saved += item
            if (items.none { it.symbol == item.symbol }) {
                items += item
            }
            return true
        }

        override fun delete(item: ShareData): Boolean {
            deleted += item
            items.removeAll { it.symbol == item.symbol }
            return true
        }

        override fun clearHistory(): Boolean {
            cleared = true
            items.clear()
            return true
        }
    }
}
