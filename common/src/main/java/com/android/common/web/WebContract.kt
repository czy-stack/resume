package com.android.common.web

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface WebContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
    }
}