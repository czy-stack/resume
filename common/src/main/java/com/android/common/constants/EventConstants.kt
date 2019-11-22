package com.android.common.constants

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 * @see com.android.common.event.Event
 * @see com.android.common.event.RxBus
 */
object EventConstants {
    //退出账户
    const val EXIT = 1
    //账户信息修改
    const val INFO_CHANGE = EXIT + 1
    //A股自选定时更新
    const val SHARE_OPTIONAL_CHANGE = INFO_CHANGE + 1
    //A股自选列表更新
    const val SHARE_OPTIONAL_LIST_CHANGE = SHARE_OPTIONAL_CHANGE + 1
    //A股创建策略
    const val SHARE_CREATE = SHARE_OPTIONAL_LIST_CHANGE + 1
    //A股购买成功
    const val SHARE_SUCCESS = SHARE_CREATE + 1
    //期货变更
    const val FUTURES_CHANGE = SHARE_SUCCESS + 1
    //期货创建策略
    const val FUTURES_CREATE = FUTURES_CHANGE + 1
    //期货tab切换
    const val FUTURES_TAB = FUTURES_CREATE + 1

}