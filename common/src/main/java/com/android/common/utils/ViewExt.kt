package com.android.common.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import com.android.common.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 view 相关扩展
 * @创建日期 2019-05-31
 * @see android.view.ViewPropertyAnimator
 * @see android.animation.Animator
 * @see android.view.animation.Animation
 */

private var toastInstance: Toast? = null

private fun Context?.showToast(text: CharSequence?, duration: Int) {
    val ctx = this ?: return
    if (text.isNullOrEmpty()) return
    toastInstance?.cancel()
    toastInstance = Toast.makeText(ctx.applicationContext, text, duration).also { it.show() }
}

fun Any.snack(view: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, this.toString(), duration).show()
}

fun Any.snack(view: View, duration: Int = Snackbar.LENGTH_INDEFINITE, listener: View.OnClickListener) {
    Snackbar.make(view, this.toString(), duration)
        .setAction(R.string.confirm, listener)
        .show()
}

/**
 * 全局复用同一个 Toast，避免连续调用时排队堆积。
 * 原实现里 `Toast(context).cancel()` 新建了一个空 Toast 再取消，既无意义也可能 NPE。
 */
fun Any.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    context.showToast(this.toString(), duration)
}

fun Context?.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(this?.getString(stringRes), duration)
}

fun View.fade(fade: Boolean) {
    if (fade) {
        if (visibility == View.VISIBLE) return
        animate().alpha(1f).duration = 400
        RxUtils.timerUI(400, TimeUnit.MILLISECONDS, Consumer {
            visibility = View.VISIBLE
        })
    } else {
        if (visibility != View.VISIBLE) return
        animate().alpha(0f).duration = 400
        RxUtils.timerUI(400, TimeUnit.MILLISECONDS, Consumer {
            visibility = View.GONE
        })
    }
}

fun View.visibility(visibility: Boolean) {
    this.visibility = if (visibility) View.VISIBLE else View.GONE
}

/**
 * 涨跌配色：红涨绿跌（A 股习惯）
 */
fun Double.setTextColorTrends(context: Context, view: TextView, float: Boolean = false) {
    view.applyTrendColor(context, this)
    view.text = if (float) {
        String.format("%.2f", this) + "%"
    } else {
        String.format("%.2f", this)
    }
}

fun TextView.setTextColorTrends(context: Context, double: Double) {
    applyTrendColor(context, double)
}

fun Double.setTextColorTrends(context: Context, double: Double, view: TextView) {
    view.applyTrendColor(context, double)
    view.text = String.format("%.2f", this)
}

/**
 * 以 this 为基准比较 double：高于基准显示绿色、低于基准显示红色
 */
fun Double.setCompareTextColorTrends(context: Context, double: Double, view: TextView) {
    when {
        double > this -> view.setTextColor(context.getCompatColor(R.color.colorLess))
        double < this -> view.setTextColor(context.getCompatColor(R.color.colorMore))
        else -> view.setTextColor(context.getCompatColor(R.color.colorBlack))
    }
    view.text = String.format("%.2f", this)
}

private fun TextView.applyTrendColor(context: Context, value: Double) {
    setTextColor(
        when {
            value > 0 -> context.getCompatColor(R.color.colorMore)
            value < 0 -> context.getCompatColor(R.color.colorLess)
            else -> context.getCompatColor(R.color.colorBlack)
        }
    )
}
