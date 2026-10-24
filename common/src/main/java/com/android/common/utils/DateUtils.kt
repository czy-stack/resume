package com.android.common.utils

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * @作者 陈忠岳
 * @主要功能 时间工具类
 * @创建日期 2019-05-31
 * @see android.text.format.DateUtils
 * @see android.text.format.DateFormat
 *
 * 相对早期实现的修正：
 * 1. 原实现持有一个 object 级共享的 `Calendar` 实例，且 `getCurrentMonthDay()` 会
 *    对它执行 `set(DATE, 1)` + `roll(-1)`，导致此后 getYear/getMonth/getDay 读到的
 *    都是被改过的日期——典型的共享可变状态缺陷；
 * 2. Calendar 本身非线程安全，放在 object 里跨线程调用存在竞态；
 * 3. 改用 java.time（minSdk 28 已支持），每次取当前时间，天然线程安全。
 */
object DateUtils {

    fun getYear(): Int = LocalDate.now().year

    /** 1-12 */
    fun getMonth(): Int = LocalDate.now().monthValue

    fun getDay(): Int = LocalDate.now().dayOfMonth

    /** 24 小时制 */
    fun getHour(): Int = LocalTime.now().hour

    fun getMinute(): Int = LocalTime.now().minute

    fun getSecond(): Int = LocalTime.now().second

    fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

    /** 当月天数 */
    fun getCurrentMonthDay(): Int = LocalDate.now().lengthOfMonth()

    /**
     * 是否工作日
     *
     * @return true 表示周一至周五
     */
    fun isWeek(): Boolean {
        val day = LocalDate.now().dayOfWeek
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY
    }

    /** 距当天 24 点还剩多久，格式 HH:mm:ss */
    fun getDateString(): String {
        val now = LocalTime.now()
        return "%02d:%02d:%02d".format(23 - now.hour, 59 - now.minute, 59 - now.second)
    }
}
