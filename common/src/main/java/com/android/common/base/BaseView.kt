package com.android.common.base

import android.view.View


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
interface BaseView<T:BasePresenter> {
    val presenter: T

    fun showToastView(message: Any)

    fun showSnackBar(view: View,message: Any)

    fun showLoading(message: String)

    fun showLoading()

    fun hiddenLoading()

    fun finish()


}