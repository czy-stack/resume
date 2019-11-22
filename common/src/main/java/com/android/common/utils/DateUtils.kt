package com.android.common.utils

import java.util.*


/**
 * @作者 陈忠岳
 * @主要功能 时间工具类
 * 复杂功能建议使用android 工具类
 * @创建日期 2019-05-31
 * @see android.text.format.DateUtils
 * @see android.text.format.DateFormat
 */
object DateUtils {
    private val instance: Calendar = Calendar.getInstance()
    private val stringBuilder: StringBuilder = StringBuilder()

    private fun getInstance(): Calendar {
        return instance
    }

    /**
     * 获取年
     *
     * @return int
     */
    fun getYear(): Int {
        return getInstance().get(Calendar.YEAR)
    }

    /**
     * 获取月
     *
     * @return int
     */
    fun getMonth(): Int {
        return getInstance().get(Calendar.MONTH) + 1
    }

    /**
     * 获取日
     *
     * @return int
     */
    fun getDay(): Int {
        return getInstance().get(Calendar.DATE)
    }

    /**
     * 获取时
     *
     * @return int
     */
    fun getHour(): Int {
        return Calendar.getInstance().get(Calendar.HOUR)
    }

    /**
     * 获取分
     *
     * @return int
     */
    fun getMinute(): Int {
        return Calendar.getInstance().get(Calendar.MINUTE)
    }

    /**
     * 获取秒
     *
     * @return int
     */
    fun getSecond(): Int {
        return Calendar.getInstance().get(Calendar.SECOND)
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return int
     */
    fun getCurrentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * 获取当月天数
     *
     * @return int
     */

    fun getCurrentMonthDay(): Int {
        return getInstance().let {
            it.set(Calendar.DATE, 1)//把日期设置为当月第一天
            it.roll(Calendar.DATE, -1)//日期回滚一天，也就是最后一天
            it.get(Calendar.DATE)
        }
    }

    /**
     * 获取已拼接的时分秒字符串
     *
     * @return String
     */
    fun getDateString(): String {
        stringBuilder.clear()
        val instance = Calendar.getInstance()
        val i = 23 - instance.get(Calendar.HOUR_OF_DAY)
        val i1 = 59 - instance.get(Calendar.MINUTE)
        val i2 = 59 - instance.get(Calendar.SECOND)
        if (i < 10) {
            stringBuilder.append(0)
        }
        stringBuilder.append(i).append(":")
        if (i1 < 10) {
            stringBuilder.append(0)
        }
        stringBuilder.append(i1).append(":")
        if (i2 < 10) {
            stringBuilder.append(0)
        }
        stringBuilder.append(i2)
        return stringBuilder.toString()
//        val simpleDateFormat = SimpleDateFormat("HH:mm:ss",Locale.getDefault())
//        获取当前时间
//        val date = Date(System.currentTimeMillis());
//        return simpleDateFormat.format(date)
    }


}