package com.android.common.base

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class BasePop(context: Context) : PopupWindow(context) {
    init {
        contentView = initView(context)
        // 设置SelectPicPopupWindow弹出窗体的宽
       width = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体的高
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        // 设置SelectPicPopupWindow弹出窗体可点击
       isFocusable = true
        // 设置SelectPicPopupWindow弹出窗体动画效果
//        animationStyle = R.style.presentActivityAnimation
        // 实例化一个ColorDrawable颜色为半透明
        val dw = ColorDrawable(0x00000000)
        // 设置SelectPicPopupWindow弹出窗体的背景
       setBackgroundDrawable(dw)
        /** 防止底部虚拟按键遮挡popup  */
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
    }

    abstract fun initView(context: Context): View

}