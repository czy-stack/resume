package com.android.common.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.android.common.R

/**
 * @作者 陈忠岳
 * @主要功能  提示弹窗
 * @创建日期  2019-11-21
 */
class HintDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val confirm: String,
    private val result: Result
) :
    Dialog(context, R.style.DialogStyle) {
    interface Result {
        fun confirm(code: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_hint)
        setCanceledOnTouchOutside(false)
        init()
    }

    private fun init() {

    }
}