package com.android.common.web

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.android.common.lifecycle.LifecyclePresenter

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class AgentWebPresenter(private val context: Context, private val view: AgentWebContract.View, lifecycleOwner: LifecycleOwner) :
    LifecyclePresenter(
        lifecycleOwner
    ), AgentWebContract.Presenter {
    override fun start() {
    }
}