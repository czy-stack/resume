package com.example.kotlin.activity.camera

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.android.common.lifecycle.LifecyclePresenter
import com.example.kotlin.activity.test.TestContract

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/8/23
 */
class CameraPresenter (private val context: Context, private val view: CameraContract.View, lifecycleOwner: LifecycleOwner)  : LifecyclePresenter(
    lifecycleOwner
), CameraContract.Presenter {
    override fun start() {
    }
}