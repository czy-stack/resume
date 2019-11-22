package com.android.common.view

import android.content.Context
import android.view.View
import android.widget.TextView
import com.android.common.R
import com.android.common.base.BasePop

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class DetailPop(context: Context) : BasePop(context) {
    private lateinit var detail: TextView
    override fun initView(context: Context): View {
        val inflate = View.inflate(context, R.layout.pop_detail, null)
        detail = inflate.findViewById(R.id.tv_detail)
        return inflate
    }

    fun setText(text: String) {
        detail.text = text
    }
}