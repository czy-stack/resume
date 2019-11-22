package com.android.common.bean

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
data class Test(
    val adminNm: String,
    val customerId: Int,
    val customerInfo: String,
    val lgnTkn: String,
    val phoneNo: String,
    val sellRsn: String,
    val sharesNm: String,
    val sharesNo: String,
    val sharesNum: Int
)

/*
{
    "customerId": 61655,
    "sharesNo": "002909",
    "sharesNm": "集泰股份",
    "sharesNum": 900,
    "sellRsn": "来自实盘：A股",
    "lgnTkn": "2019-11-21 13:21:53",
    "customerInfo": "1360***7",
    "phoneNo": "草根",
    "adminNm": "买入集泰股份(002909)"
}*/
