package com.example.kotlin.activity.test

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView
import com.example.kotlin.bean.ShareData

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface TestContract {

    interface View : BaseView<Presenter> {
        fun setShares(list: List<ShareData>, history: Boolean = false)
    }

    interface Presenter : BasePresenter {
        fun getShares(search: String, type: Int)
    }

}