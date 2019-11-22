package com.android.common.inter

import com.google.android.material.appbar.AppBarLayout

/**
 * @作者 陈忠岳
 * @主要功能 监听AppBar状态
 * @创建日期  2019-11-21
 */
abstract class AppBarStateChangeListener : AppBarLayout.OnOffsetChangedListener {
    enum class State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }

    private var mCurrentState: State = State.IDLE
    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        appBarLayout?.let {
            if (verticalOffset == 0) {
                if (mCurrentState != State.EXPANDED) {
                    onStateChanged(it, State.EXPANDED)
                }
                mCurrentState = State.EXPANDED
            } else if (Math.abs(verticalOffset) >= it.totalScrollRange) {
                if (mCurrentState != State.COLLAPSED) {
                    onStateChanged(it, State.COLLAPSED)
                }
                mCurrentState = State.COLLAPSED
            } else {
                if (mCurrentState != State.IDLE) {
                    onStateChanged(it, State.IDLE)
                }
                mCurrentState = State.IDLE
            }
        }
    }

    abstract fun onStateChanged(appBarLayout: AppBarLayout, state: State)
}