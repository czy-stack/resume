package com.android.common.utils

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.android.common.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @作者 陈忠岳
 * @主要功能 view工具类
 * @创建日期 2019-05-31
 * @see android.view.ViewPropertyAnimator
 * @see android.animation.Animator
 * @see android.view.animation.Animation
 * https://blog.csdn.net/guolin_blog/article/details/44171115
 */


fun Any.snack(view: View, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(view, this.toString(), duration).show()
}

fun Any.snack(view: View, duration: Int = Snackbar.LENGTH_INDEFINITE,listener :View.OnClickListener) {
    Snackbar.make(view, this.toString(), duration)
        .setAction("朕晓得了",listener).show()
}

fun Any.toast(context: Context?, duration: Int = Toast.LENGTH_SHORT) {
    Toast(context).cancel()
    Toast.makeText(context, this.toString(), duration).show()
}


fun Context?.toast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast(this).cancel()
    Toast.makeText(this, this?.getString(stringRes), duration).show()
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
    if (visibility) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.GONE
    }
}

fun Double.setTextColorTrends(context: Context, view: TextView, float: Boolean = false) {
    if (this > 0) {
        view.setTextColor(context.getCompatColor(R.color.colorMore))
    } else if (this < 0) {
        view.setTextColor(context.getCompatColor(R.color.colorLess))
    } else {
        view.setTextColor(context.getCompatColor(R.color.colorBlack))
    }
    view.text = if (float) {
        StringBuilder(String.format("%.2f", this)).append("%")
    } else {
        String.format("%.2f", this)
    }

}

fun TextView.setTextColorTrends(context: Context, double: Double) {
    if (double > 0) {
        this.setTextColor(context.getCompatColor(R.color.colorMore))
    } else if (double < 0) {
        this.setTextColor(context.getCompatColor(R.color.colorLess))
    } else {
        this.setTextColor(context.getCompatColor(R.color.colorBlack))
    }
}

fun Double.setTextColorTrends(context: Context, double: Double, view: TextView) {
    if (double > 0) {
        view.setTextColor(context.getCompatColor(R.color.colorMore))
    } else if (double < 0) {
        view.setTextColor(context.getCompatColor(R.color.colorLess))
    } else {
        view.setTextColor(context.getCompatColor(R.color.colorBlack))
    }
    view.text = String.format("%.2f", this)
}

fun Double.setCompareTextColorTrends(context: Context, double: Double, view: TextView) {
    if (double > this) {
        view.setTextColor(context.getCompatColor(R.color.colorLess))
    } else if (double < this) {
        view.setTextColor(context.getCompatColor(R.color.colorMore))
    } else {
        view.setTextColor(context.getCompatColor(R.color.colorBlack))
    }
    view.text = String.format("%.2f", this)
}


