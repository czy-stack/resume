package com.example.kotlin.activity.test

import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.common.base.BaseActivity
import com.android.common.constants.EventConstants
import com.android.common.event.Event
import com.android.common.event.Subscribe
import com.android.common.event.rxRegister
import com.android.common.event.rxUnregister
import com.example.kotlin.R
import com.example.kotlin.adapter.HistorySearchAdapter
import com.example.kotlin.adapter.SearchAdapter
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.databinding.ActivityTestBinding

/**
 * @作者 陈忠岳
 * @主要功能 行情搜索页
 * @创建日期  2019-11-21
 *
 * 这里只做三件事：把交互转成 Presenter 的调用、把数据渲染到列表、订阅跨页面事件。
 * 防抖、去重、读写数据库都在 Presenter 内部完成。
 */
class TestActivity : BaseActivity<TestContract.Presenter, ActivityTestBinding>(), TestContract.View {

    override lateinit var presenter: TestContract.Presenter

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var historyAdapter: HistorySearchAdapter

    override fun setBinding(): ActivityTestBinding {
        return ActivityTestBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = TestPresenter(this, this)

        searchAdapter = SearchAdapter { item -> presenter.onShareSelected(item) }
        historyAdapter = HistorySearchAdapter(
            onItemClick = { item -> presenter.onShareSelected(item) },
            onDeleteClick = { item -> presenter.onHistoryDeleted(item) }
        )

        binding.recView.apply {
            layoutManager = LinearLayoutManager(this@TestActivity)
            addItemDecoration(
                DividerItemDecoration(this@TestActivity, LinearLayoutManager.VERTICAL)
            )
            adapter = historyAdapter
        }
    }

    override fun initData() {
        binding.rbTwo.isChecked = true
        rxRegister()
    }

    override fun initListener() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.onKeywordSubmitted(query.orEmpty())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter.onKeywordChanged(newText.orEmpty())
                return true
            }
        })

        binding.tvClear.setOnClickListener { presenter.onClearHistory() }

        binding.radio.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_two -> presenter.onTypeChanged(MainConstants.HK)
                R.id.rb_three -> presenter.onTypeChanged(MainConstants.USA)
            }
        }
    }

    override fun showShares(list: List<ShareData>, fromHistory: Boolean) {
        if (fromHistory) {
            binding.recView.adapter = historyAdapter
            binding.tvTip.text = if (list.isEmpty()) {
                getString(com.android.common.R.string.string_history_empty)
            } else {
                getString(com.android.common.R.string.string_history)
            }
            binding.tvClear.isVisible = list.isNotEmpty()
            historyAdapter.submitList(list)
        } else {
            binding.recView.adapter = searchAdapter
            binding.tvTip.text = if (list.isEmpty()) {
                getString(com.android.common.R.string.string_search_empty)
            } else {
                getString(com.android.common.R.string.string_search_result)
            }
            binding.tvClear.isVisible = false
            searchAdapter.submitList(list)
        }
    }

    /**
     * 示例：其他页面点击结果后发出事件，这里接收并提示
     * （原先用 EventBus 的 @Subscribe，现统一到 RxBus）
     */
    @Subscribe
    fun onEvent(event: Event) {
        if (event.type == EventConstants.FUTURES_TAB) {
            showToastView("已选择  ${event.message}")
        }
    }

    override fun onDestroy() {
        rxUnregister()
        super.onDestroy()
    }
}
