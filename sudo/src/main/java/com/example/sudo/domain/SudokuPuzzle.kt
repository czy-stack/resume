package com.example.sudo.domain

import com.example.sudo.computationlogic.buildNewSudoku
import java.io.Serializable
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/27
 */
data class SudokuPuzzle(
    val boundary : Int,
    val difficulty : Difficulty,
    val graph : LinkedHashMap<Int , LinkedList<SudokuNode>>
    = buildNewSudoku(boundary , difficulty).graph,
    var elapsedTime: Long = 0L
):Serializable{
    fun getValue(): LinkedHashMap<Int, LinkedList<SudokuNode>> = graph
}
