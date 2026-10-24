package com.android.common.base

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.android.common.R
import com.android.common.utils.RxUtils
import com.android.common.utils.snack
import com.android.common.utils.toast
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 Activity 基类：ViewBinding + MVP 生命周期编排
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. 删掉了从未被调用的 `getLayoutId()` 抽象方法——页面内容由 [setBinding] 决定，
 *    两套方式并存只会让子类误以为必须实现（此前子类返回 0 也能正常跑）；
 * 2. `colorStatusBar` 里 `A; B` 的分号第二个表达式被丢弃，实际未生效；
 * 3. `openScreenOn()` 传 0 时仍会启动计时器，导致常亮刚设置就被关闭；
 * 4. 常亮计时器改为主线程并在 onDestroy 取消，避免页面销毁后回调仍持有 window。
 */
abstract class BaseActivity<T : BasePresenter, B : ViewBinding> : AppCompatActivity(), BaseView<T> {

    override lateinit var presenter: T
    lateinit var binding: B

    private var screenOnTimer: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setBinding()
        setContentView(binding.root)
        init()
    }

    @NonNull
    protected abstract fun setBinding(): B

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

    override fun showSnackBar(view: View, message: Any) {
        message.snack(view)
    }

    override fun showLoading(message: String) {
    }

    override fun showLoading() {
    }

    override fun hiddenLoading() {
    }

    /**
     * app 全屏：隐藏状态栏、导航栏与输入法，滑动可临时呼出
     */
    protected fun windowFullScreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.apply {
            hide(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
            systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    /**
     * 状态栏颜色，默认透明
     */
    @Suppress("DEPRECATION")
    protected fun colorStatusBar(@ColorRes color: Int = R.color.transparent) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    /**
     * 保持屏幕常亮
     *
     * @param millisecond 保持时长，传 0 表示直到手动关闭
     */
    protected fun openScreenOn(millisecond: Int = 0) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (millisecond > 0) {
            startTimer(millisecond)
        }
    }

    /**
     * 取消屏幕常亮
     */
    protected fun closeScreenOn() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startTimer(millisecond: Int) {
        screenOnTimer?.dispose()
        screenOnTimer = RxUtils.timerUI(millisecond.toLong(), TimeUnit.MILLISECONDS) {
            closeScreenOn()
        }
    }

    override fun onDestroy() {
        screenOnTimer?.dispose()
        screenOnTimer = null
        super.onDestroy()
    }
}
