package com.android.common.http

import com.google.gson.annotations.SerializedName

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
data class ResultBean<T>(val code: Int, @SerializedName("errmsg")val error: String, val response: T)