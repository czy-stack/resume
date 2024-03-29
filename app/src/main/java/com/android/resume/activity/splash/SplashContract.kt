package com.android.resume.activity.splash

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView
import com.android.resume.databinding.ActivitySplashBinding

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface SplashContract {

    interface View : BaseView<Presenter> {

    }

    interface Presenter : BasePresenter {
        fun saveInfoAll()
    }

}