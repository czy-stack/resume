package com.android.common.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.android.common.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.BaseRequestOptions

/**
 * @作者 陈忠岳
 * @主要功能 Glide 图片加载扩展
 * @创建日期 2019-08-02
 */

/** 不缓存的图片加载，适合头像、验证码等一次性资源 */
fun ImageView.glideNoneCache(url: String) {
    Glide.with(this).load(url)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .error(R.mipmap.icon_img_failured)
        .into(this)
}

fun ImageView.glideCache(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.glideCache(@DrawableRes resId: Int) {
    Glide.with(this).load(resId).into(this)
}

fun ImageView.glideCache(url: String, requestOptions: BaseRequestOptions<*>) {
    Glide.with(this).load(url).apply(requestOptions).into(this)
}
