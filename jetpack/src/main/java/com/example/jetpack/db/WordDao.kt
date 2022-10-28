package com.example.jetpack.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jetpack.vo.Word
import kotlinx.coroutines.flow.Flow

/**
 * @作者 陈忠岳
 * @主要功能 room dao
 * 在 DAO（数据访问对象）中，您可以指定 SQL 查询并将其与方法调用相关联。编译器会检查 SQL 并根据常见查询的方便的注解（如 @Insert）生成查询。Room 会使用 DAO 为代码创建整洁的 API。
 * DAO 必须是一个接口或抽象类。
 * 默认情况下，所有查询都必须在单独的线程上执行。
 * Room 支持 Kotlin 协程，您可使用 suspend 修饰符对查询进行注解，然后从协程或其他挂起函数对其进行调用。
 * @创建日期  2022/9/22
 */
@Dao
interface WordDao {

    @Query("SELECT * FROM word_table ORDER BY word ASC")
    fun  getAlphabetizedWords(): Flow<List<Word>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(words: List<Word>)

    @Delete
    suspend fun delete(word: Word)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(word: Word)

    @Query("DELETE FROM word_table")
    suspend fun deleteAll()

}