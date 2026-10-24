package com.android.common.utils

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * 金额计算测试。
 *
 * 行情/交易类项目里浮点误差会直接变成金额差错，这里锁住三点：
 * 1. 不会因为 Double 二进制表示产生 0.30000000000000004 这类尾差；
 * 2. 采用银行家舍入（1.15 -> 1.2，1.25 -> 1.2）；
 * 3. 除法按指定精度直接舍入，不再二次 setScale。
 */
class NumberExtTest {

    @Test
    fun `加法消除浮点尾差`() {
        assertEquals(0.3, 0.1.add(0.2, 2), 0.0)
    }

    @Test
    fun `减法保留指定小数位`() {
        assertEquals(1.23, 1.234.sub(0.004, 2), 0.0)
    }

    @Test
    fun `乘法按银行家舍入`() {
        // 1.25 * 1 保留 1 位：HALF_EVEN 下 2 为偶数，不进位
        assertEquals(1.2, 1.25.mul(1.0, 1), 0.0)
        // 1.15 * 1 保留 1 位：HALF_EVEN 下向前进一位
        assertEquals(1.2, 1.15.mul(1.0, 1), 0.0)
    }

    @Test
    fun `除法的精度由参数决定`() {
        assertEquals(0.33, 1.0.div(3.0, 2), 0.0)
        assertEquals(0.3333, 1.0.div(3.0, 4), 0.0)
    }

    @Test(expected = ArithmeticException::class)
    fun `除零抛出 ArithmeticException`() {
        1.0.div(0.0, 2)
    }

    @Test
    fun `负数同样遵循银行家舍入`() {
        assertEquals(-1.2, (-1.25).mul(1.0, 1), 0.0)
    }
}
