package com.android.resume.activity.test

import android.view.View
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.common.base.BaseActivity
import com.android.common.constants.EventConstants
import com.android.common.event.Event
import com.android.common.inter.OnItemClickListener
import com.android.main.adapter.HistorySearchAdapter
import com.android.main.adapter.SearchAdapter
import com.android.resume.R
import com.android.resume.bean.ShareData
import com.android.resume.constants.MainConstants
import com.android.resume.db.DBManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_test.*
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class TestActivity : BaseActivity<TestContract.Presenter>(), TestContract.View, LifecycleOwner ,
    OnItemClickListener {
    override fun setShares(list: List<ShareData>, history: Boolean) {
        if (history) {
            rec_view.adapter = historySearchAdapter
            tv_tip.text = getString(R.string.string_history)
            tv_clear.visibility = View.VISIBLE
            historySearchAdapter.list = list
            historySearchAdapter.notifyDataSetChanged()
        } else {
            rec_view.adapter = adapter
            tv_tip.text = "搜索结果"
            tv_clear.visibility = View.GONE
            adapter.list = list
            adapter.notifyDataSetChanged()
        }
    }

    override lateinit var presenter: TestContract.Presenter

    private lateinit var adapter: SearchAdapter
    private lateinit var historySearchAdapter: HistorySearchAdapter
    private val subject = PublishSubject.create<String>()
    private var notes = ""
    private var type = MainConstants.H

    override fun getLayoutId(): Int {
        return R.layout.activity_test
    }

    override fun initView() {
        presenter = TestPresenter(this, this, this)
        rec_view.layoutManager = LinearLayoutManager(this)
        rec_view.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        subject.debounce(800, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { search -> notes != search }
            .doOnNext { search -> notes = search; presenter.getShares(search, type) }
            .subscribe()
        rb_two.isChecked = true
    }

    override fun initData() {
        adapter = SearchAdapter(this, listOf())
        historySearchAdapter = HistorySearchAdapter(this, listOf())
        tv_tip.text = getString(R.string.string_history)
        presenter.getShares("", type)
    }

    override fun initListener() {
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {// 当点击搜索按钮时触发该方法
                if (!query.isNullOrEmpty() && notes != query) {
                    notes = query
                    presenter.getShares(query, type)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {// 当搜索内容改变时触发该方法
                if (newText != null) {
                    subject.onNext(newText)
                }
                return false
            }
        })
        tv_clear.setOnClickListener {
            DBManager.clear()
            historySearchAdapter.clear()
            tv_clear.visibility = View.GONE
        }
        adapter.addItemClickListener(this)
        radio.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_two -> {
                    if (type != MainConstants.H) {
                        type = MainConstants.H
                        presenter.getShares(notes, MainConstants.H)
                    }
                }
                R.id.rb_three -> {
                    if (type != MainConstants.M) {
                        type = MainConstants.M
                        presenter.getShares(notes, MainConstants.M)
                    }
                }
            }
        }
    }


    override fun onItemClick(position: Int, type: Int) {
    }

    @Subscribe
    fun event(event :Event){
        if (event.type == EventConstants.FUTURES_TAB){
            showToastView("点击  "+event.message)
        }
    }
}