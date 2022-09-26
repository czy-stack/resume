package com.example.jetpack.repository.inDb

import androidx.annotation.WorkerThread
import com.example.jetpack.db.WordDao
import com.example.jetpack.vo.Word
import kotlinx.coroutines.flow.Flow

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/22
 */
class WordRepository (private val wordDao: WordDao) {
    val allWords : Flow<List<Word>> = wordDao.getAlphabetizedWords()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
}