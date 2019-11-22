package com.android.common.lifecycle

import androidx.lifecycle.LifecycleOwner
import com.android.common.utils.LogUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class LifecyclePresenter(private val mLifecycleOwner: LifecycleOwner) : FullLifecycleObserver {
    //常用rxJava disposable
    private var compositeDisposable: CompositeDisposable? = null


    init {
        init()
    }
    private fun init(){
        LogUtils.i("Lifecycle_init", mLifecycleOwner.javaClass.toString())
        mLifecycleOwner.lifecycle.addObserver(FullLifecycleObserverAdapter(mLifecycleOwner, this))
    }

    override fun onCreate(owner: LifecycleOwner) {
        LogUtils.i("Lifecycle_onCreate", owner.javaClass.toString())
    }

    override fun onStart(owner: LifecycleOwner) {
    }

    override fun onResume(owner: LifecycleOwner) {
    }

    override fun onPause(owner: LifecycleOwner) {
    }

    override fun onStop(owner: LifecycleOwner) {
    }

    override fun onDestroy(owner: LifecycleOwner) {
        LogUtils.i("Lifecycle_onDestroy", owner.javaClass.toString())
        clearDisposable()
    }

    protected fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

    protected fun clearDisposable() {
        compositeDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
                it.clear()
            }
        }
    }
}