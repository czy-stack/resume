package com.android.common.http

import android.content.Context
import android.net.ParseException
import android.text.TextUtils
import com.android.common.R
import com.android.common.utils.LogUtils
import com.android.common.utils.toast
import com.android.common.BuildConfig
import com.google.gson.JsonParseException
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class RxObserver<T>(val context: Context) : Observer<T> {
    private var disposable: Disposable? = null
    override fun onComplete() {
        disposable?.let {
            if (it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onSubscribe(d: Disposable) {
        disposable = d
    }

    override fun onError(e: Throwable) {
        if (BuildConfig.DEBUG) {
            LogUtils.i(msg = e.message.toString())
        }
        /**
         * 获取根源 异常
         */
        if (e is HttpException) {
            if (TextUtils.isEmpty(e.message)) {
                context.getString(R.string.toast_common_parse_error) .toast(context)
            } else {
                if (TextUtils.isEmpty(e.message)) {
                    context.getString(R.string.toast_common_parse_error).toast(context)
                } else {
                    e.message?.toast(context)
                }
            }
        } else if (e is ApiException){
            onResultError(e)
        }else if (e is JsonParseException || e is JSONException || e is ParseException) {
            context.getString(R.string.toast_common_parse_error).toast(context)
        } else if (e is UnknownHostException) {
            context.getString(R.string.toast_common_server_error).toast(context)
        } else if (e is SocketTimeoutException) {
//            R.string.toast_common_net_timeout.toast(context)
        } else {
//            R.string.toast_common_net_error.toast(context)
            e.printStackTrace()
        }
        disposable?.let {
            if (it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onNext(resultBean: T) {
        if (resultBean==null){
            onError(JsonParseException(context.getString(R.string.toast_common_parse_error)))
        }else{
            onSuccess(resultBean)
        }
    }

    abstract fun onSuccess(resultBean: T)

    private fun onResultError(ex:ApiException){
        ex.let {
            if (it.code==-1){
                context.getString(R.string.login_invalid_error).toast(context)
            }else if (it.message.isNullOrEmpty()){
//                context.getString(R.string.toast_common_net_error).toast(context)
            }else {
                it.message?.toast(context)
            }
        }
    }

}