package com.example.kotlin.db

import com.example.kotlin.bean.Record
import com.example.kotlin.bean.ShareData
import org.litepal.LitePal
import org.litepal.extension.deleteAll
import org.litepal.extension.find
import org.litepal.extension.findAll

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2019-11-22
 */
object DBManager {

    fun getHistory(): List<ShareData> {
        return LitePal.findAll<ShareData>()
    }

    fun save(data: ShareData) {
//        LogUtils.i(msg = data.toString())
        if (!data.isSaved) {
            data.save()
        }
    }

    fun delete(data: ShareData) {
        if (data.isSaved) {
            data.delete()
        }
    }

    fun clear() {
        LitePal.deleteAll<ShareData>()
    }

    fun searchRecord(typeId: Int, search: String, count: Int = 100): List<Record> {
        return LitePal.where("typeId = ? and (name like ? or symbol like ? or pinYin like ?)", typeId.toString(),
            "%$search%","%$search%","%$search%").limit(count).find()
    }
}