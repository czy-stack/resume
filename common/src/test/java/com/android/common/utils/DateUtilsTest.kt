package com.android.common.utils

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * 时间工具测试。
 *
 * 原实现持有 object 级共享 Calendar，且 getCurrentMonthDay() 会改写它，
 * 导致后续 getYear/getMonth/getDay 全部错乱。这里验证各方法互不影响。
 * 只断言与「当前时间无关」的性质，避免跨天/跨时区时的偶发失败。
 */
class DateUtilsTest {

    @Test
    fun `当月天数在合法区间内`() {
        val days = DateUtils.getCurrentMonthDay()
        assertTrue("当月天数应介于 28~31，实际 $days", days in 28..31)
    }

    @Test
    fun `获取当月天数不会污染其他取值`() {
        val yearBefore = DateUtils.getYear()
        val monthBefore = DateUtils.getMonth()
        val dayBefore = DateUtils.getDay()

        DateUtils.getCurrentMonthDay()

        assertEquals(yearBefore, DateUtils.getYear())
        assertEquals(monthBefore, DateUtils.getMonth())
        assertEquals(dayBefore, DateUtils.getDay())
    }

    @Test
    fun `月份在 1 到 12 之间`() {
        assertTrue(DateUtils.getMonth() in 1..12)
    }

    @Test
    fun `小时分钟秒在合法区间`() {
        assertTrue(DateUtils.getHour() in 0..23)
        assertTrue(DateUtils.getMinute() in 0..59)
        assertTrue(DateUtils.getSecond() in 0..59)
    }

    @Test
    fun `倒计时字符串格式为 HH mm ss`() {
        val text = DateUtils.getDateString()
        assertTrue("格式不符：$text", Regex("""\d{2}:\d{2}:\d{2}""").matches(text))
    }

    @Test
    fun `isWeek 与星期几一致`() {
        val dayOfWeek = java.time.LocalDate.now().dayOfWeek
        val isWeekend = dayOfWeek == java.time.DayOfWeek.SATURDAY ||
            dayOfWeek == java.time.DayOfWeek.SUNDAY
        assertEquals("工作日判定与 java.time 不一致", !isWeekend, DateUtils.isWeek())
    }
}
