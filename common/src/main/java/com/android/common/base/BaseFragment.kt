package com.android.common.base

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
 * @主要功能 Fragment 基类
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. 删掉了构造期求值的 `mContext`（`super.getContext()` 在 Fragment attach 前恒为 null）
 *    以及只被注释代码使用的 `appContext`；
 * 2. [finish] 不再直接强转 `AppCompatActivity`，宿主不是 AppCompat 时只回退 pop 而不会崩；
 * 3. `init()` 改到 `super.onViewCreated` 之后调用，符合 Fragment 生命周期顺序。
 */
abstract class BaseFragment<T : BasePresenter> : Fragment(), BaseView<T> {

    abstract override val presenter: T

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView()

    protected abstract fun initData()

    protected abstract fun initListener()

    private fun init() {
        initView()
        presenter.start()
        initData()
        initListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
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
        when (val host = context) {
            is AppCompatActivity -> host.finish()
            else -> parentFragmentManager.popBackStack()
        }
    }
}
