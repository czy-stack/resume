package com.example.sign.activity

import com.android.common.base.BasePresenter
import com.android.common.base.BaseView

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2020/12/10
 */
interface SignContract {

    interface View:BaseView<Presenter>{

    }

    interface Presenter :BasePresenter{

    }
}