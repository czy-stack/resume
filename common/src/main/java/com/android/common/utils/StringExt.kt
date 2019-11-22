package com.android.common.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.ColorInt
import com.android.common.constants.Constants

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */


/**
 * 部分字体变色
 *
 * @param text  需要变色的 String
 * @param color 颜色
 * @param start 起始位置
 * @param end  结束位置
 */
fun StringBuilder.colorSpan(@ColorInt color: Int, start: Int, end: Int): SpannableStringBuilder {
    return SpannableStringBuilder(this).apply {
        setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    }
}

fun String.textSize(view: TextView) {
    val text = this.trim()
    val length = text.length
    if (length < 5) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
    } else if (length < 8) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
    } else if (length < 12) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
    } else {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
    }
    view.text = text
}



