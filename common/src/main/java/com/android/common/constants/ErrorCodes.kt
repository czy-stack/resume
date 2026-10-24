package com.android.common.constants

import android.content.Context
import androidx.annotation.StringRes
import com.android.common.R

/**
 * @作者 陈忠岳
 * @主要功能 业务错误码 → 用户文案的单一映射
 * @创建日期 2026-10-24
 *
 * 早期实现把同一份 30 行的 when 抄在 [com.android.common.http.RxSimpleObserver]
 * 和 `String.getError()` 两处，改一处必然漏一处；这里收敛为唯一来源。
 *
 * 生产项目中这套文案通常由服务端下发或走多语言资源，演示项目内联为资源文件即可。
 */
object ErrorCodes {

    const val PARAM_INVALID = "-1"
    const val ACCOUNT_EXISTS = "-2"
    const val OPERATION_FAILED = "-3"
    const val PASSWORD_INCORRECT = "-4"
    const val NO_PERMISSION = "-5"
    const val LOGIN_REQUIRED = "-6"
    const val ACCOUNT_DISABLED = "-7"
    const val BANK_CARD_MISSING = "-8"
    const val INSUFFICIENT_BALANCE = "-9"
    const val RISK_STOCK_FORBIDDEN = "-10"
    const val LEVERAGE_UNSUPPORTED = "-11"
    const val DAILY_QUOTA_EXCEEDED = "-12"
    const val VERIFY_CODE_INVALID = "-13"
    const val TOO_FREQUENT = "-14"
    const val SUSPENDED_STOCK_FORBIDDEN = "-15"
    const val LOT_SIZE_TOO_SMALL = "-16"
    const val NOT_TRADING_TIME = "-17"
    const val REQUEST_ERROR = "-18"
    const val RED_PACKET_LOCKED = "-19"
    const val NOT_APPLY_TIME = "-20"
    const val RENEW_ONLY_ON_EXPIRY = "-21"
    const val NO_BUY_ON_EXPIRY = "-22"
    const val INSUFFICIENT_QUOTA = "-23"
    const val BANK_VERIFY_FAILED = "-24"
    const val POSITION_LIMIT_EXCEEDED = "-25"
    const val NOT_CONTEST_TIME = "-26"
    const val NOT_ELIGIBLE = "-27"
    const val FREE_CAPITAL_NOT_ALLOWED = "-28"
    const val RESET_ONLY_IN_REGULAR = "-29"
    const val QUALIFICATION_TOO_LOW = "-30"

    /** 未收录的业务码返回 null，由调用方决定兜底文案 */
    @StringRes
    fun messageRes(code: String): Int? = when (code) {
        PARAM_INVALID -> R.string.error_code_param_invalid
        ACCOUNT_EXISTS -> R.string.error_code_account_exists
        OPERATION_FAILED -> R.string.error_code_operation_failed
        PASSWORD_INCORRECT -> R.string.error_code_password_incorrect
        NO_PERMISSION -> R.string.error_code_no_permission
        LOGIN_REQUIRED -> R.string.error_code_login_required
        ACCOUNT_DISABLED -> R.string.error_code_account_disabled
        BANK_CARD_MISSING -> R.string.error_code_bank_card_missing
        INSUFFICIENT_BALANCE -> R.string.error_code_insufficient_balance
        RISK_STOCK_FORBIDDEN -> R.string.error_code_risk_stock_forbidden
        LEVERAGE_UNSUPPORTED -> R.string.error_code_leverage_unsupported
        DAILY_QUOTA_EXCEEDED -> R.string.error_code_daily_quota_exceeded
        VERIFY_CODE_INVALID -> R.string.error_code_verify_code_invalid
        TOO_FREQUENT -> R.string.error_code_too_frequent
        SUSPENDED_STOCK_FORBIDDEN -> R.string.error_code_suspended_stock_forbidden
        LOT_SIZE_TOO_SMALL -> R.string.error_code_lot_size_too_small
        NOT_TRADING_TIME -> R.string.error_code_not_trading_time
        REQUEST_ERROR -> R.string.error_code_request_error
        RED_PACKET_LOCKED -> R.string.error_code_red_packet_locked
        NOT_APPLY_TIME -> R.string.error_code_not_apply_time
        RENEW_ONLY_ON_EXPIRY -> R.string.error_code_renew_only_on_expiry
        NO_BUY_ON_EXPIRY -> R.string.error_code_no_buy_on_expiry
        INSUFFICIENT_QUOTA -> R.string.error_code_insufficient_quota
        BANK_VERIFY_FAILED -> R.string.error_code_bank_verify_failed
        POSITION_LIMIT_EXCEEDED -> R.string.error_code_position_limit_exceeded
        NOT_CONTEST_TIME -> R.string.error_code_not_contest_time
        NOT_ELIGIBLE -> R.string.error_code_not_eligible
        FREE_CAPITAL_NOT_ALLOWED -> R.string.error_code_free_capital_not_allowed
        RESET_ONLY_IN_REGULAR -> R.string.error_code_reset_only_in_regular
        QUALIFICATION_TOO_LOW -> R.string.error_code_qualification_too_low
        else -> null
    }

    fun messageRes(code: Int): Int? = messageRes(code.toString())

    fun messageOf(context: Context, code: String): String? =
        messageRes(code)?.let { context.getString(it) }

    fun messageOf(context: Context, code: Int): String? = messageOf(context, code.toString())
}
