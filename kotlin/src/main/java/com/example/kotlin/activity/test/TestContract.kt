package com.example.kotlin.activity.test

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView
import com.example.kotlin.bean.ShareData

/**
 * @作者 陈忠岳
 * @主要功能 行情搜索页的 MVP 契约
 * @创建日期  2019-11-21
 *
 * 调整：原契约只有 `getShares(search, type)`，把防抖、类型切换、历史读取全塞在一个方法里，
 * 导致 View 必须自己处理交互细节、Presenter 也无法区分「用户连续输入」和「点了搜索」。
 * 现在按用户意图拆成独立入口，防抖与去重逻辑下沉到 Presenter。
 */
interface TestContract {

    interface View : BaseView<Presenter> {
        /**
         * @param fromHistory true 表示这是历史记录，false 表示搜索结果
         */
        fun showShares(list: List<ShareData>, fromHistory: Boolean)
    }

    interface Presenter : BasePresenter {
        /** 输入框内容变化（走防抖） */
        fun onKeywordChanged(keyword: String)

        /** 点击搜索键（立即查询，不走防抖） */
        fun onKeywordSubmitted(keyword: String)

        /** 切换市场（港股 / 美股） */
        fun onTypeChanged(type: Int)

        /** 点击某条结果：写入历史并通知外部 */
        fun onShareSelected(item: ShareData)

        /** 删除单条历史 */
        fun onHistoryDeleted(item: ShareData)

        /** 清空历史 */
        fun onClearHistory()
    }
}
