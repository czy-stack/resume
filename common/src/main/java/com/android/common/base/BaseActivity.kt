package com.android.common.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.common.R
import com.android.common.utils.snack
import com.android.common.utils.toast

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class BaseActivity<T : BasePresenter> : AppCompatActivity(), BaseView<T> {

    abstract override val presenter: T


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    private fun init() {
        initView()
        presenter.start()
        initData()
        initListener()
    }

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    override fun showToastView(message: Any) {
        message.toast(this)
    }

    override fun showLoading(message: String) {
    }

    override fun showLoading() {
    }

    override fun hiddenLoading() {
    }

    override fun showSnackBar(view: View, message: Any) {
        message.snack(view)
    }

    /**
     * 透明状态栏
     */
    protected fun colorStatusBar( @ColorRes color: Int = R.color.transparent) {
        window.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.statusBarColor = ContextCompat.getColor(this, color)
        }
    }
}