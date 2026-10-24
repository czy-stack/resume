package com.android.common.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.common.utils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @作者 陈忠岳
 * @主要功能 跟随宿主生命周期的 Presenter 基类
 * @创建日期  2019-11-21
 *
 * 说明：宿主销毁时统一释放 Rx 订阅，避免 Presenter 持有 View 导致泄漏。
 * 构造参数允许为 null，这样纯逻辑可以在 JVM 单元测试中直接实例化，不必伪造 LifecycleOwner。
 */
abstract class LifecyclePresenter(private val lifecycleOwner: LifecycleOwner?) : DefaultLifecycleObserver {

    private var compositeDisposable: CompositeDisposable? = null
    private val tag = "Lifecycle_"

    init {
        lifecycleOwner?.lifecycle?.addObserver(this)
        LogUtils.d(tag + "init", lifecycleOwner?.javaClass?.name ?: "no-owner")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        LogUtils.d(tag + "onDestroy", owner.javaClass.name)
        clearDisposable()
    }

    protected fun addDisposable(disposable: Disposable) {
        val composite = compositeDisposable ?: CompositeDisposable().also { compositeDisposable = it }
        composite.add(disposable)
    }

    protected fun clearDisposable() {
        compositeDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
            it.clear()
        }
        compositeDisposable = null
    }
}
