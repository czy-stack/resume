package com.android.common.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.android.common.R
import com.android.common.databinding.DialogConfirmBinding

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
    private lateinit var binding :DialogConfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.let {
            it.tvTitle.text = title
            it.tvMessage.text = message
            it.tvCancel.text = cancel
            it.tvConfirm.text = confirm
            it.tvConfirm.setOnClickListener {
                result.confirm()
                cancel()
            }
            it.tvCancel.setOnClickListener {
                result.cancel()
                cancel()
            }
        }
    }
}