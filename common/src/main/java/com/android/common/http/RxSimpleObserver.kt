package com.android.common.http

import android.content.Context
import com.android.common.BuildConfig
import com.android.common.constants.ErrorCodes
import com.android.common.utils.LogUtils
import com.android.common.utils.toast
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @作者 陈忠岳
 * @主要功能 不区分 HTTP 状态的简单订阅者：只关心「成功」和「业务错误」
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. 原实现把整个响应体 `toString()` 拿去和 "-1" 之类的错误码做相等比较，
 *    永远匹配不上，错误提示分支实际是死代码；改为按 [ResultBean.code] 判断；
 * 2. 释放逻辑同样取反，已修正；
 * 3. 不再在这里维护一份错误码 when，统一走 [ErrorCodes]。
 */
abstract class RxSimpleObserver<T>(protected val context: Context) : Observer<T> {

    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onComplete() {
        release()
    }

    override fun onNext(resultBean: T) {
        LogUtils.i("onNext", resultBean.toString())

        val errorCode = (resultBean as? ResultBean<*>)?.code
        val message = errorCode
            ?.takeIf { it < 0 }
            ?.let { ErrorCodes.messageOf(context, it) }

        if (message.isNullOrBlank()) {
            onSuccess(resultBean)
        } else {
            onFailure(message)
        }
    }

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) {
            LogUtils.e(msg = e.message ?: e.toString())
        }
        release()
        onFailure(e.toUserMessage(context))
    }

    abstract fun onSuccess(resultBean: T)

    protected open fun onFailure(message: String) {
        message.toast(context)
    }

    private fun release() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        disposable = null
    }
}
