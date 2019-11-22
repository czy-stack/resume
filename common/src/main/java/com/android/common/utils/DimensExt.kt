package com.android.common.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.DimenRes

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */

/**
 * dp转换像素
 *
 * @param context
 */
fun Float.dpToPx(context: Context):Float{
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this,context.resources.displayMetrics)
}

@DimenRes
fun Int.dpToPx(context: Context): Int{
    return context.resources.getDimensionPixelSize(this)
}
