package com.android.common.constants

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * 错误码映射测试。
 *
 * 原实现有两份完全重复的 when（RxSimpleObserver 与 String.getError），
 * 且用「整个响应体字符串」去和错误码比较，永远不会命中。
 * 这里锁住映射的完整性与边界行为。
 */
class ErrorCodesTest {

    private val handledCodes = listOf(
        ErrorCodes.PARAM_INVALID,
        ErrorCodes.ACCOUNT_EXISTS,
        ErrorCodes.OPERATION_FAILED,
        ErrorCodes.PASSWORD_INCORRECT,
        ErrorCodes.NO_PERMISSION,
        ErrorCodes.LOGIN_REQUIRED,
        ErrorCodes.ACCOUNT_DISABLED,
        ErrorCodes.BANK_CARD_MISSING,
        ErrorCodes.INSUFFICIENT_BALANCE,
        ErrorCodes.RISK_STOCK_FORBIDDEN,
        ErrorCodes.LEVERAGE_UNSUPPORTED,
        ErrorCodes.DAILY_QUOTA_EXCEEDED,
        ErrorCodes.VERIFY_CODE_INVALID,
        ErrorCodes.TOO_FREQUENT,
        ErrorCodes.SUSPENDED_STOCK_FORBIDDEN,
        ErrorCodes.LOT_SIZE_TOO_SMALL,
        ErrorCodes.NOT_TRADING_TIME,
        ErrorCodes.REQUEST_ERROR,
        ErrorCodes.RED_PACKET_LOCKED,
        ErrorCodes.NOT_APPLY_TIME,
        ErrorCodes.RENEW_ONLY_ON_EXPIRY,
        ErrorCodes.NO_BUY_ON_EXPIRY,
        ErrorCodes.INSUFFICIENT_QUOTA,
        ErrorCodes.BANK_VERIFY_FAILED,
        ErrorCodes.POSITION_LIMIT_EXCEEDED,
        ErrorCodes.NOT_CONTEST_TIME,
        ErrorCodes.NOT_ELIGIBLE,
        ErrorCodes.FREE_CAPITAL_NOT_ALLOWED,
        ErrorCodes.RESET_ONLY_IN_REGULAR,
        ErrorCodes.QUALIFICATION_TOO_LOW
    )

    @Test
    fun `全部 30 个业务码都能映射到文案资源`() {
        assertEquals(30, handledCodes.size)
        handledCodes.forEach { code ->
            assertNotNull("错误码 $code 缺少文案映射", ErrorCodes.messageRes(code))
        }
    }

    @Test
    fun `错误码不重复`() {
        assertEquals("错误码常量存在重复定义", handledCodes.size, handledCodes.toSet().size)
    }

    @Test
    fun `未收录的错误码返回 null 而不是乱码`() {
        assertNull(ErrorCodes.messageRes("9999"))
        assertNull(ErrorCodes.messageRes(""))
        assertNull(ErrorCodes.messageRes("abc"))
    }

    @Test
    fun `Int 重载与 String 版本结果一致`() {
        assertEquals(ErrorCodes.messageRes("-1"), ErrorCodes.messageRes(-1))
        assertEquals(ErrorCodes.messageRes("-30"), ErrorCodes.messageRes(-30))
    }

    @Test
    fun `成功码不在错误映射中`() {
        assertNull("0 表示成功，不应有错误文案", ErrorCodes.messageRes("0"))
    }
}
