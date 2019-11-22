package com.android.common.http

import android.content.Context
import android.net.ParseException
import android.text.TextUtils
import com.android.common.BuildConfig
import com.android.common.constants.TypeConstants
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
 * @主要功能
 * @创建日期  2019-11-21
 */
abstract class RxSimpleObserver<T>(val context: Context) : Observer<T> {
    private var disposable: Disposable? = null
    override fun onComplete() {
        disposable?.let {
            if (!it.isDisposed) {
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
//                R.string.toast_common_parse_error.toast(context)
            } else {
                if (TextUtils.isEmpty(e.message)) {
//                    R.string.toast_common_parse_error.toast(context)
                } else {
                    e.message?.toast(context)
                }
            }
        } else if (e is JsonParseException || e is JSONException || e is ParseException) {
//            R.string.toast_common_parse_error.toast(context)
        } else if (e is UnknownHostException) {
//            R.string.toast_common_server_error.toast(context)
        } else if (e is SocketTimeoutException) {
//            R.string.toast_common_net_timeout.toast(context)
        } else {
//            context.getString(R.string.toast_common_net_error).toast(context)
            e.printStackTrace()
        }
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onNext(resultBean: T) {
        LogUtils.i("onNext", resultBean.toString())
        getError(resultBean.toString()).let {
            if (it.isBlank()){
                onSuccess(resultBean)
            }else{
                it.toast(context)
            }
        }
    }

    abstract fun onSuccess(resultBean: T)

    private fun getError(code: String) :String{
        when (code) {
            TypeConstants.CODE1 -> return  "上传参数错误"
            TypeConstants .CODE2 -> return  "该账号已注册"
            TypeConstants .CODE3 -> return  "操作执行失败"
            TypeConstants .CODE4 -> return  "原来密码不对"
            TypeConstants .CODE5 -> return  "没有这个权限"
            TypeConstants .CODE6 -> return  "请重新登录"
            TypeConstants .CODE7 -> return  "用户被禁用了"
            TypeConstants .CODE8 -> return  "银行卡未填写"
            TypeConstants .CODE9 -> return  "余额不够"
            TypeConstants .CODE10 -> return  "风险股票不能买入"
            TypeConstants .CODE11 -> return  "不支持这个杠杆"
            TypeConstants .CODE12 -> return  "已经达到每天点买的最大额度数"
            TypeConstants .CODE13 -> return  "验证码错误"
            TypeConstants .CODE14 -> return  "操作太频繁"
            TypeConstants .CODE15 -> return  "停牌股票不能卖出"
            TypeConstants .CODE16 -> return  "交易额不足买入100股"
            TypeConstants .CODE17 -> return  "当前非可交易时间"
            TypeConstants .CODE18 -> return  "请求错误"
            TypeConstants .CODE19 -> return  "红包需要交易后才能解锁提现"
            TypeConstants .CODE20 -> return  "非可申请时间"
            TypeConstants .CODE21 -> return  "只能在合约到期当日续约"
            TypeConstants .CODE22 -> return  "合约到期当日不可买入"
            TypeConstants .CODE23 -> return  "可买入额度不足"
            TypeConstants .CODE24 -> return  "银行身份验证错误"
            TypeConstants .CODE25 -> return  "单股持仓不可超过总额的50"
            TypeConstants .CODE26 -> return  "当前非比赛交易时间"
            TypeConstants .CODE27 -> return  "不符合领取条件"
            TypeConstants .CODE28 -> return  "免费的实盘资金不能添加"
            TypeConstants .CODE29 -> return  "仅常规赛期间可以重置资金"
            TypeConstants .CODE30 -> return  "请先提升你的资质评分"
            else -> return  ""
        }
    }

}