package com.example.jetpack

import android.app.Application
import com.example.jetpack.db.WordRoomDatabase
import com.example.jetpack.repository.inDb.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/23
 */
open class WordsApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { WordRoomDatabase.getDatabase(this,applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}