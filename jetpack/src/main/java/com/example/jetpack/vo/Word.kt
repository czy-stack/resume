package com.example.jetpack.vo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @作者 陈忠岳
 * @主要功能  view object/value object    前端展示的数据，在接口数据返回给前端的时候需要转成VO
 * @创建日期  2022/9/22
 */
@Entity(tableName = "word_table")
data class Word(@PrimaryKey @ColumnInfo(name = "word")val word: String)