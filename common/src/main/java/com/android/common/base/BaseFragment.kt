package com.android.common.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.common.utils.snack
import com.android.common.utils.toast

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class BaseFragment<T : BasePresenter> : Fragment(), BaseView<T> {
    abstract override val presenter: T

    private val mContext:Context? = super.getContext()

    private lateinit var appContext :Context

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun showToastView(message: Any) {
        message.toast(context)
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

    override fun finish() {
        (context as AppCompatActivity).finish()
    }

    override fun getContext(): Context {
        if (mContext!=null){
            appContext = mContext.applicationContext
            return mContext
        }else if (super.getContext()!=null){
            return super.getContext()!!
        }else{
            return appContext
        }
    }
}