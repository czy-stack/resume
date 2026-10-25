package com.android.common.http

import android.content.Context
import android.net.ParseException
import com.android.common.BuildConfig
import com.android.common.R
import com.android.common.constants.ErrorCodes
import com.android.common.utils.LogUtils
import com.android.common.utils.toast
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @作者 陈忠岳
 * @主要功能 统一的 Rx 请求订阅者
 * @创建日期  2019-11-21
 *
 * 相对早期实现的修正：
 * 1. [onComplete]、[onError] 里的释放逻辑取反了（`if (isDisposed) dispose()`），
 *    实际从不释放；这里统一走 [release] 并置空引用；
 * 2. HttpException 分支里同一个 `isEmpty(message)` 判断嵌套了两层，属于复制粘贴；
 * 3. 超时、网络不可达两个分支原本被注释掉，用户得不到任何反馈，现统一给出提示；
 * 4. 失败文案抽到 [toUserMessage]，与业务错误码共用一份映射。
 */
abstract class RxObserver<T : Any>(protected val context: Context) : Observer<T> {

    private var disposable: Disposable? = null

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onComplete() {
        release()
    }

    override fun onNext(resultBean: T) {
        if (resultBean == null) {
            onFailure(context.getString(R.string.toast_common_parse_error))
        } else {
            onSuccess(resultBean)
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

    /** 失败回调。默认直接 toast；子类可覆写为交给 View 层统一展示 */
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

/**
 * 把异常翻译成可以直接展示给用户的文案。
 * 集中在一处，避免每个 Observer 各写一套 when。
 */
internal fun Throwable.toUserMessage(context: Context): String = when (this) {
    is ApiException -> ErrorCodes.messageOf(context, code)
        ?: message.orEmpty().ifBlank { context.getString(R.string.toast_common_net_error) }

    is HttpException -> context.getString(R.string.toast_common_server_error)
    is JsonParseException, is JSONException, is ParseException ->
        context.getString(R.string.toast_common_parse_error)

    is UnknownHostException -> context.getString(R.string.toast_common_server_error)
    is SocketTimeoutException -> context.getString(R.string.toast_common_net_timeout)
    else -> context.getString(R.string.toast_common_net_error)
}
