package com.android.common.base

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import com.android.common.R

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class BaseToolbar : Toolbar {
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, @Nullable attrs: AttributeSet?) : this(context, attrs, R.attr.toolbarStyle)

    constructor(context: Context?, @Nullable attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inflateMenu(R.menu.menu_toolbar)
        //        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        //        setTitleTextAppearance(getContext(),R.style.ToolBarTitleTextStyle);
        //        setNavigationIcon(R.drawable.icon_break_selector);
    }

    fun showRightRes(vararg itemId: Int) {
        for (item in itemId) {
            menu.findItem(item).isVisible = true
        }
    }

    fun goneRightRes(vararg itemId: Int) {
        for (item in itemId) {
            menu.findItem(item).isVisible = false
        }
    }
}