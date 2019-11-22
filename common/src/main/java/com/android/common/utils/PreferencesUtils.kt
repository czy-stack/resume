package com.android.common.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-21
 */
object PreferencesUtils {
    private const val PREFERENCE_NAME = "RESUME"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun putString(context: Context, key: String, value: String) {
        getSharedPreferences(context).edit().let {
            it.putString(key, value)
            it.apply()
        }
    }

    fun getString(context: Context, key: String): String? {
        return getSharedPreferences(context).getString(key, null)
    }

    fun putInt(context: Context, key: String, value: Int) {
        getSharedPreferences(context).edit().let {
            it.putInt(key, value)
            it.apply()
        }
    }

    fun getInt(context: Context, key: String): Int {
        return getSharedPreferences(context).getInt(key, 0)
    }

    fun putBoolean(context: Context, key: String, value: Boolean) {
        getSharedPreferences(context).edit().let {
            it.putBoolean(key, value)
            it.apply()
        }
    }

    fun getBoolean(context: Context, key: String): Boolean {
        return getSharedPreferences(context).getBoolean(key, false)
    }

}