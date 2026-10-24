package com.example.kotlin.data

import com.example.kotlin.bean.Record
import com.example.kotlin.bean.ShareData
import com.example.kotlin.db.DBManager

/**
 * @作者 陈忠岳
 * @主要功能 [ShareRepository] 的 LitePal 实现
 * @创建日期 2026-10-24
 *
 * 注意：LitePal 的查询是同步的，调用方需要自行切到 IO 线程。
 * 演示项目数据量小（本地缓存的自选列表），暂不引入协程/Room 的异步查询。
 */
class LitePalShareRepository : ShareRepository {

    override fun history(): List<ShareData> = DBManager.getHistory()

    override fun search(type: Int, keyword: String, limit: Int): List<ShareData> {
        if (keyword.isBlank()) return emptyList()
        return DBManager.searchRecord(type, keyword, limit)
            .map { it.toShareData(fallbackType = type) }
    }

    override fun save(item: ShareData): Boolean {
        DBManager.save(item)
        return true
    }

    override fun delete(item: ShareData): Boolean {
        DBManager.delete(item)
        return true
    }

    override fun clearHistory(): Boolean {
        DBManager.clear()
        return true
    }
}

/**
 * 搜索结果 -> 列表项模型
 *
 * 原实现把 `type` 写成查询类型常量（港股结果一律标 HK），
 * 这里优先用记录自身的 typeId，缺失时才回退到查询类型。
 */
internal fun Record.toShareData(fallbackType: Int): ShareData = ShareData(
    symbol = mId.toString(),
    name = name,
    describe = symbol,
    type = if (typeId != 0) typeId else fallbackType
)
