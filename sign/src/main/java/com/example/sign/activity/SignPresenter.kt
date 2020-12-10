package com.example.sign.activity

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.android.common.lifecycle.LifecyclePresenter

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2020/12/10
 */
class SignPresenter(
    private val context: Context,
    private val view: SignContract.View,
    lifecycleOwner: LifecycleOwner
) : LifecyclePresenter(lifecycleOwner),SignContract.Presenter {
    override fun start() {
    }
}