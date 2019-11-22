package com.android.common.base

import com.android.common.BuildConfig


/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
object BaseUrls {
    const val BASE_URL: String = BuildConfig.API_HOST

    //新浪行情请求
    const val XL_URL = "https://hq.sinajs.cn/"
    //网易分时请求
    const val MINUTE_URL = "https://img1.money.126.net/data/hs/time/today/"
    //阿里云分时请求
    const val FUTURES_MINUTE_URL = "https://qchart.oss-cn-hangzhou.aliyuncs.com/"
    //阿里云K线请求
    const val FUTURES_K_URL = "https://qchart.oss-cn-hangzhou.aliyuncs.com/kxt/"

    //K线请求
    const val K_URL = "https://pdfm.eastmoney.com/EM_UBG_PDTI_Fast/api/js/"
}
