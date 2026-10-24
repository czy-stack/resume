package com.android.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @作者 陈忠岳
 * @主要功能 金额/价格计算扩展
 * @创建日期 2019-08-02
 *
 * 行情类项目中浮点误差会直接变成金额差错，这里统一走 BigDecimal 并采用银行家舍入
 * （ROUND_HALF_EVEN：1.15 -> 1.2，1.25 -> 1.2）。
 *
 * 修正：原实现用 `BigDecimal(Double)` 构造，会把 0.1 展开成 0.1000000000000000055…，
 * 应当使用 `BigDecimal.valueOf()`（内部走 Double.toString，语义更接近预期）。
 */
private fun Double.bd(): BigDecimal = BigDecimal.valueOf(this)

fun Double.add(d2: Double, point: Int): Double =
    bd().add(d2.bd()).setScale(point, RoundingMode.HALF_EVEN).toDouble()

fun Double.sub(d2: Double, point: Int): Double =
    bd().subtract(d2.bd()).setScale(point, RoundingMode.HALF_EVEN).toDouble()

fun Double.mul(d2: Double, point: Int): Double =
    bd().multiply(d2.bd()).setScale(point, RoundingMode.HALF_EVEN).toDouble()

fun Double.div(d2: Double, point: Int): Double =
    bd().divide(d2.bd(), point, RoundingMode.HALF_EVEN).toDouble()
