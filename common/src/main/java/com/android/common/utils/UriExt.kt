package com.android.common.utils

import android.content.Context
import android.net.Uri
import java.io.IOException

/**
 * @作者 陈忠岳
 * @主要功能 Uri 读取扩展
 * @创建日期 2022-11-04
 *
 * 从原 LumberExt 中拆出（那个文件同时塞了 Glide、EventBus、正则和 BigDecimal）。
 * 顺带去掉了 `openInputStream(this)!!` 的强制解包，避免 Uri 失效时直接 NPE。
 */

@Throws(IOException::class)
fun Uri.readBytes(context: Context): ByteArray? =
    context.contentResolver.openInputStream(this)?.use { it.buffered().readBytes() }

fun Uri.uriToByte(context: Context): ByteArray? = readBytes(context)
