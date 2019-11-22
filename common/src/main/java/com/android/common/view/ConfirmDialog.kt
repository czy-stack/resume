package com.android.common.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.android.common.R
import kotlinx.android.synthetic.main.dialog_confirm.*

/**
 * @作者 陈忠岳
 * @主要功能  确认弹窗
 * @创建日期  2019-11-21
 */
class ConfirmDialog(
    context: Context,
    private val title: String,
    private val message: String,
    private val result: Result,
    private val confirm: String = "确定",
    private val cancel: String = "取消"
) :
    Dialog(context, R.style.DialogStyle) {
    interface Result {
        fun confirm()
        fun cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm)
        init()
    }

    private fun init() {
        tv_title.text = title
        tv_message.text = message
        tv_cancel.text = cancel
        tv_confirm.text = confirm
        tv_confirm.setOnClickListener {
            result.confirm()
            cancel()
        }
        tv_cancel.setOnClickListener {
            result.cancel()
            cancel()
        }
    }
}