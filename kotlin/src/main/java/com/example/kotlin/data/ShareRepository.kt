package com.example.kotlin.data

import com.example.kotlin.bean.ShareData

/**
 * @作者 陈忠岳
 * @主要功能 自选/历史数据的读写入口
 * @创建日期 2026-10-24
 *
 * 之前 Adapter 与 Activity 直接调用 `DBManager`，导致「MVP 分层」只停留在
 * BaseActivity/BasePresenter 的壳子上：View 层能直接摸到数据库，Presenter 也无法被替换。
 * 这里抽成接口，生产实现走 LitePal，单元测试用内存 Fake。
 */
interface ShareRepository {

    /** 历史记录，按写入顺序返回（旧 -> 新） */
    fun history(): List<ShareData>

    /** 按类型 + 关键词搜索，[limit] 为结果上限 */
    fun search(type: Int, keyword: String, limit: Int = DEFAULT_LIMIT): List<ShareData>

    /** 收藏/记录一条，已存在则忽略 */
    fun save(item: ShareData): Boolean

    fun delete(item: ShareData): Boolean

    fun clearHistory(): Boolean

    companion object {
        const val DEFAULT_LIMIT = 100
    }
}
