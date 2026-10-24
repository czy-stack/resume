package com.example.kotlin.activity.test

import androidx.lifecycle.LifecycleOwner
import com.android.common.constants.EventConstants
import com.android.common.event.Event
import com.android.common.event.RxBus
import com.android.common.lifecycle.LifecyclePresenter
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.data.LitePalShareRepository
import com.example.kotlin.data.ShareRepository
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 行情搜索页 Presenter
 * @创建日期  2019-11-21
 *
 * 重构要点：
 * 1. 防抖从 Activity 挪到这里，View 只负责把输入事件转过来，交互逻辑可被单测覆盖；
 * 2. 依赖 [ShareRepository] 而不是直接调用 DBManager，测试时可注入内存实现；
 * 3. 调度器通过构造参数注入，单元测试传 TestScheduler 即可精确控制时间，
 *    不必依赖 Android 主线程；
 * 4. 选中结果通过 [RxBus] 广播，替代原先 Adapter 直接 post EventBus 的写法。
 */
class TestPresenter(
    private val view: TestContract.View,
    lifecycleOwner: LifecycleOwner? = null,
    private val repository: ShareRepository = LitePalShareRepository(),
    private val timerScheduler: Scheduler = Schedulers.computation(),
    private val uiScheduler: Scheduler = AndroidSchedulers.mainThread(),
    private val debounceMillis: Long = DEFAULT_DEBOUNCE_MILLIS,
    /** 事件出口，默认走 RxBus；单测里可替换成空实现，避免初始化 Android 主线程调度器 */
    private val eventPublisher: (Event) -> Unit = { RxBus.instance.post(it) }
) : LifecyclePresenter(lifecycleOwner), TestContract.Presenter {

    companion object {
        const val DEFAULT_DEBOUNCE_MILLIS = 800L
    }

    private data class Query(val keyword: String, val type: Int)

    private val queryStream = PublishSubject.create<Query>()

    private var currentType: Int = MainConstants.HK
    private var lastQuery: String? = null

    init {
        addDisposable(
            queryStream
                .debounce(debounceMillis, TimeUnit.MILLISECONDS, timerScheduler)
                .observeOn(uiScheduler)
                // 同一个关键词不重复查询，避免用户停顿后再次触发
                .filter { it.keyword != lastQuery }
                .subscribe { query ->
                    lastQuery = query.keyword
                    performSearch(query.keyword, query.type)
                }
        )
    }

    override fun start() {
        showHistory()
    }

    override fun onKeywordChanged(keyword: String) {
        if (keyword.isEmpty()) {
            showHistory()
            return
        }
        queryStream.onNext(Query(keyword, currentType))
    }

    override fun onKeywordSubmitted(keyword: String) {
        if (keyword.isEmpty()) {
            showHistory()
            return
        }
        if (keyword == lastQuery) return
        lastQuery = keyword
        performSearch(keyword, currentType)
    }

    override fun onTypeChanged(type: Int) {
        if (type == currentType) return
        currentType = type

        val keyword = lastQuery.orEmpty()
        if (keyword.isEmpty()) {
            showHistory()
        } else {
            performSearch(keyword, type)
        }
    }

    override fun onShareSelected(item: ShareData) {
        repository.save(item)
        eventPublisher(Event(message = item.name, type = EventConstants.FUTURES_TAB))
    }

    override fun onHistoryDeleted(item: ShareData) {
        repository.delete(item)
        showHistory()
    }

    override fun onClearHistory() {
        repository.clearHistory()
        showHistory()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        queryStream.onComplete()
        super.onDestroy(owner)
    }

    private fun showHistory() {
        lastQuery = null
        view.showShares(repository.history().reversed(), true)
    }

    private fun performSearch(keyword: String, type: Int) {
        view.showShares(repository.search(type, keyword), false)
    }
}
