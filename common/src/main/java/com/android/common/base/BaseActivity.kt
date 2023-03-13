package com.android.common.base

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding
import com.android.common.R
import com.android.common.databinding.ActivityWebBinding
import com.android.common.utils.RxUtils
import com.android.common.utils.snack
import com.android.common.utils.toast
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class BaseActivity<T : BasePresenter,B:ViewBinding> : AppCompatActivity(), BaseView<T> {

    override lateinit var presenter: T
    lateinit var binding : B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setBinding()
        setContentView(binding.root)
        init()
    }

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    @NonNull
    protected abstract fun setBinding() :B

//    @NonNull
//    protected abstract fun setPresenter() :T


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
     * app全屏
     */
    protected fun windowFullScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)
            windowInsetsController?.let {
                it.hide(WindowInsetsCompat.Type.statusBars())
                it.hide(WindowInsetsCompat.Type.ime())
                it.hide(WindowInsetsCompat.Type.systemBars())
                it.hide(WindowInsetsCompat.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    /**
     * 状态栏颜色 默认透明
     */
    protected fun colorStatusBar( @ColorRes color: Int = R.color.transparent) {
        window.let {
            it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            it.statusBarColor = ContextCompat.getColor(this, color)
        }
    }

    /**
     * 保持屏幕常亮
     *
     * @param millisecond 设置亮的毫秒数
     *
     */
    protected fun openScreenOn(millisecond:Int = 0){
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        startTimer(millisecond)
    }

    /**
     * 取消屏幕常亮
     *
     */
    protected fun closeScreenOn(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun startTimer(millisecond: Int) {
        RxUtils.timer(millisecond.toLong(),TimeUnit.MILLISECONDS) {
            closeScreenOn()
        }
    }

}