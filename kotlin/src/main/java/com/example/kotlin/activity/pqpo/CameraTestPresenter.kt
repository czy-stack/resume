package com.example.kotlin.activity.pqpo

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.android.common.lifecycle.LifecyclePresenter

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraTestPresenter (private val context: Context, private val view: CameraTestContract.View, lifecycleOwner: LifecycleOwner)  : LifecyclePresenter(
    lifecycleOwner
), CameraTestContract.Presenter {
    override fun start() {
    }
}