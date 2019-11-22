package com.android.common.utils

import android.content.Context
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.android.common.R
import com.android.common.constants.TypeConstants
import com.android.common.event.Event
import com.android.common.event.RxBus
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.BaseRequestOptions
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import java.math.BigDecimal
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-08-02
 */

/**
 * 不缓存的Glide图片加载
 */
fun ImageView.glideNoneCache(context:Context,url:String) {
    Glide.with(context).load(url)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.mipmap.icon_img_failured).into(this)
}

/**
 * Glide图片加载
 */
fun ImageView.glideCache(context:Context,url:String) {
    Glide.with(context).load(url).into(this)
}

fun ImageView.glideCache(context:Context,@DrawableRes url:Int) {
    Glide.with(context).load(url).into(this)
}

/**
 * Glide图片加载
 */
fun ImageView.glideCache(context:Context,url:String,requestOptions: BaseRequestOptions<*>) {
    Glide.with(context).load(url).apply(requestOptions).into(this)
}


/**
 * 校验手机号
 * @return 校验通过返回true，否则返回false
 */
fun CharSequence.isMobile() :Boolean{
    return Pattern.matches("^(0(10|2\\d|[3-9]\\d\\d)[- ]{0,3}\\d{7,8}|0?1[3584]\\d{9})$", this);
}

/**
* event post
*/
fun Event.post(){
    EventBus.getDefault().post(this)
}

/**
 * event register
 */
fun Any?.register(){
    if (this == null){
        return
    }
    LogUtils.i("register",msg = this.toString())
    if (!EventBus.getDefault().isRegistered(this)){
        EventBus.getDefault().register(this)
    }
}

/**
 * event unregister
 */
fun Any?.unregister(){
    if (this == null){
        return
    }
    LogUtils.i("unregister",msg = this.toString())
    if (EventBus.getDefault().isRegistered(this)){
        EventBus.getDefault().unregister(this)
    }
}


fun Context.getCompatColor(@ColorRes color: Int): Int{
    return ContextCompat.getColor(this,color)
}

/*
* ROUND_UP
舍入远离零的舍入模式。
在丢弃非零部分之前始终增加数字(始终对非零舍弃部分前面的数字加1)。
注意，此舍入模式始终不会减少计算值的大小。

ROUND_DOWN
接近零的舍入模式。
在丢弃某部分之前始终不增加数字(从不对舍弃部分前面的数字加1，即截短)。
注意，此舍入模式始终不会增加计算值的大小。

ROUND_CEILING
接近正无穷大的舍入模式。
如果 BigDecimal 为正，则舍入行为与 ROUND_UP 相同;
如果为负，则舍入行为与 ROUND_DOWN 相同。
注意，此舍入模式始终不会减少计算值。

ROUND_FLOOR
接近负无穷大的舍入模式。
如果 BigDecimal 为正，则舍入行为与 ROUND_DOWN 相同;
如果为负，则舍入行为与 ROUND_UP 相同。
注意，此舍入模式始终不会增加计算值。

ROUND_HALF_UP
向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为向上舍入的舍入模式。
如果舍弃部分 >= 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同。
注意，这是我们大多数人在小学时就学过的舍入模式(四舍五入)。

ROUND_HALF_DOWN
向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则为上舍入的舍入模式。
如果舍弃部分 > 0.5，则舍入行为与 ROUND_UP 相同;否则舍入行为与 ROUND_DOWN 相同(五舍六入)。

ROUND_HALF_EVEN 银行家舍入法
向“最接近的”数字舍入，如果与两个相邻数字的距离相等，则向相邻的偶数舍入。
如果舍弃部分左边的数字为奇数，则舍入行为与 ROUND_HALF_UP 相同;
如果为偶数，则舍入行为与 ROUND_HALF_DOWN 相同。
注意，在重复进行一系列计算时，此舍入模式可以将累加错误减到最小。
此舍入模式也称为“银行家舍入法”，主要在美国使用。四舍六入，五分两种情况。
如果前一位为奇数，则入位，否则舍去。
以下例子为保留小数点1位，那么这种舍入方式下的结果。
1.15>1.2 1.25>1.2

ROUND_UNNECESSARY
断言请求的操作具有精确的结果，因此不需要舍入。
如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
* */
fun Double.add(d2:Double,point:Int):Double{
    return BigDecimal(this).add(BigDecimal(d2)).setScale(point,BigDecimal.ROUND_HALF_EVEN).toDouble()
}
fun Double.sub(d2:Double,point:Int):Double{
    return BigDecimal(this).subtract(BigDecimal(d2)).setScale(point,BigDecimal.ROUND_HALF_EVEN).toDouble()
}
fun Double.mul(d2:Double,point:Int):Double{
    return BigDecimal(this).multiply(BigDecimal(d2)).setScale(point,BigDecimal.ROUND_HALF_EVEN).toDouble()
}
fun Double.div(d2:Double,point:Int):Double{
    return BigDecimal(this).divide(BigDecimal(d2)).setScale(point,BigDecimal.ROUND_HALF_EVEN).toDouble()
}

fun String.getError():String{
    when (this) {
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


