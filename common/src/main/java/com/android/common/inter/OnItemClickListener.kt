package com.android.common.inter

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-08-05
 */
interface OnItemClickListener {
    fun onItemClick(position: Int, type: Int)
}
interface OnItemLongClickListener {
    fun onItemLongClick(position: Int, type: Int)
}