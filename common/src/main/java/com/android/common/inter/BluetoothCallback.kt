package com.android.common.inter

import android.bluetooth.le.ScanResult

/**
 * @作者 陈忠岳
 * @主要功能 蓝牙接口回调
 * @创建日期  2022/9/5
 */
interface DiscoveryCallback {
    fun onFindBle(callbackType:Int, result: ScanResult)
    fun onNotFind()
}