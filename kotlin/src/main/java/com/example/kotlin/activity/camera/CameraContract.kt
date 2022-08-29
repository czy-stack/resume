package com.example.kotlin.activity.camera

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView
import com.example.kotlin.bean.ShareData

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraContract {

    interface View : BaseView<Presenter> {
    }

    interface Presenter : BasePresenter {
    }

}