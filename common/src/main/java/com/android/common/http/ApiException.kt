package com.android.common.http

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
class ApiException(val code:Int,string: String) :Exception(string)