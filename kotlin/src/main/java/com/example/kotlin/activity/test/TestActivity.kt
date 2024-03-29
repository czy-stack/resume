package com.example.kotlin.activity.test

import android.view.View
import android.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.common.base.BaseActivity
import com.android.common.constants.EventConstants
import com.android.common.event.Event
import com.android.common.inter.OnItemClickListener
import com.android.resume.activity.test.TestPresenter
import com.example.kotlin.R
import com.example.kotlin.adapter.HistorySearchAdapter
import com.example.kotlin.adapter.SearchAdapter
import com.example.kotlin.bean.ShareData
import com.example.kotlin.constants.MainConstants
import com.example.kotlin.databinding.ActivityTestBinding
import com.example.kotlin.db.DBManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class TestActivity : BaseActivity<TestContract.Presenter,ActivityTestBinding>(), TestContract.View, LifecycleOwner ,
    OnItemClickListener {
    override fun setShares(list: List<ShareData>, history: Boolean) {
        if (history) {
            binding.recView.adapter = historySearchAdapter
            binding.tvTip.text = getString(R.string.string_history)
            binding.tvClear.visibility = View.VISIBLE
            historySearchAdapter.list = list
            historySearchAdapter.notifyDataSetChanged()
        } else {
            binding.recView.adapter = adapter
            binding.tvTip.text = "搜索结果"
            binding.tvClear.visibility = View.GONE
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
    override fun setBinding(): ActivityTestBinding {
        return ActivityTestBinding.inflate(layoutInflater)
    }

    override fun initView() {
        presenter = TestPresenter(this, this, this)
        binding.recView.layoutManager = LinearLayoutManager(this)
        binding.recView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        subject.debounce(800, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .filter { search -> notes != search }
            .doOnNext { search -> notes = search; presenter.getShares(search, type) }
            .subscribe()
        binding.rbTwo.isChecked = true
    }

    override fun initData() {
        adapter = SearchAdapter(this, listOf())
        historySearchAdapter = HistorySearchAdapter(this, listOf())
        binding.tvTip.text = getString(R.string.string_history)
        presenter.getShares("", type)
    }

    override fun initListener() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        binding.tvClear.setOnClickListener {
            DBManager.clear()
            historySearchAdapter.clear()
            binding.tvClear.visibility = View.GONE
        }
        adapter.addItemClickListener(this)
        binding.radio.setOnCheckedChangeListener { group, checkedId ->
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