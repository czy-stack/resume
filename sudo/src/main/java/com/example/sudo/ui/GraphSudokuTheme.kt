package com.example.sudo.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/10
 */

private val LightColorPalette = lightColors(
    primary = primaryGreen,
    secondary = textColorLight,
    surface = lightGrey,
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber
)

private val DarkColorPalette = darkColors(
    //main background color
    primary = primaryCharcoal,
    //used for text color
    secondary = textColorDark,
    //background of sudoku board
    surface = lightGreyAlpha,
    //grid lines of sudoku board
    primaryVariant = gridLineColorLight,
    onPrimary = accentAmber,
    onSurface = accentAmber
)

@Composable
fun GraphSudokuTheme(
    darkTheme:Boolean = isSystemInDarkTheme(),
    content : @Composable () -> Unit
){
    MaterialTheme(
        colors = if (darkTheme) DarkColorPalette else LightColorPalette,
        typography = typography,
        shapes = shapes,
        content = content
    )
}