package com.android.common.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

/**
 * @作者 陈忠岳
 * @主要功能 文本校验与颜色取值
 * @创建日期 2019-08-02
 */

/**
 * 手机号 / 固话校验
 * 支持：13812345678、010-12345678、0755-1234567 等
 */
fun CharSequence.isMobile(): Boolean =
    Pattern.matches("^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$", this)

fun Context.getCompatColor(@ColorRes color: Int): Int = ContextCompat.getColor(this, color)
