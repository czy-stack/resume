package com.android.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @作者 陈忠岳
 * @主要功能  不能左右滑动  ViewPager
 * @创建日期  2019-11-21
 * @see androidx.viewpager2.widget.ViewPager2
 */
class NoSlideViewPage : ViewPager {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return false
    }
}