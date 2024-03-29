package com.example.sudo.ui.activegame

import android.util.Log
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.android.common.utils.LogUtils
import com.example.sudo.ui.components.AppToolbar
import com.example.sudo.R
import com.example.sudo.common.toTime
import com.example.sudo.computationlogic.sqrt
import com.example.sudo.ui.*
import com.example.sudo.ui.components.LoadingScreen

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/10
 */

enum class ActiveGameScreenState {
    LOADING,
    ACTIVE,
    COMPLETE
}

@Composable
fun ActiveGameScreen(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel
) {

    val contentTransitionState = remember {
        MutableTransitionState(
            ActiveGameScreenState.LOADING
        )
    }

    viewModel.subContentState = {
        contentTransitionState.targetState = it
    }

    Log.i("ActiveGameScreen",contentTransitionState.targetState.name +"    "+contentTransitionState.currentState.name )

    val transition = updateTransition(contentTransitionState, label = "")

    val loadingAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""
    ) {
        if (it == ActiveGameScreenState.LOADING) 1f else 0f
    }

    val activeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""
    ) {
        if (it == ActiveGameScreenState.ACTIVE) 1f else 0f
    }

    val completeAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = ""
    ) {
        if (it == ActiveGameScreenState.COMPLETE) 1f else 0f
    }

    Column(
        Modifier
            .background(MaterialTheme.colors.primary)
            .fillMaxHeight()
    ) {

        AppToolbar(
            modifier = Modifier
                .wrapContentHeight(),
            title = stringResource(R.string.app_name),
        ) {
            NewGameIcon(onEventHandler = onEventHandler)
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .padding(top = 4.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            when (contentTransitionState.currentState) {
                ActiveGameScreenState.ACTIVE -> Box(
                    Modifier
                        .alpha(activeAlpha)
                ) {
                    GameContent(
                        onEventHandler,
                        viewModel
                    )
                }
                ActiveGameScreenState.COMPLETE -> Box(
                    Modifier
                        .alpha(completeAlpha)
                ) {
                    GameCompleteContent(
                        viewModel.timerState,
                        viewModel.isNewRecordState
                    )
                }
                ActiveGameScreenState.LOADING -> Box(
                    Modifier
                        .alpha(loadingAlpha)
                ) {
                    LoadingScreen()
                }
            }
        }
    }
}

@Composable
fun NewGameIcon(onEventHandler: (ActiveGameEvent) -> Unit) {
    Icon(
        imageVector = Icons.Filled.Add,
        tint = if (MaterialTheme.colors.isLight) textColorLight else textColorDark,
        contentDescription = null,
        modifier = Modifier
            .clickable(onClick = {
                onEventHandler.invoke(
                    ActiveGameEvent.OnNewGameClicked
                )
            }
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(36.dp),
    )
}

@Composable
fun GameContent(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel
) {
    BoxWithConstraints {

        val screenWidth = with(LocalDensity.current) {
            constraints.maxWidth.toDp()
        }

        val margin = with(LocalDensity.current) {
            when {
                constraints.maxHeight.toDp().value < 500f -> 20
                constraints.maxHeight.toDp().value < 550f -> 8
                else -> 0
            }
        }

        ConstraintLayout {

            val (board, timer, diff, inputs) = createRefs()

            Box(Modifier
                .constrainAs(board) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .background(MaterialTheme.colors.surface)
                .size(screenWidth - margin.dp)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colors.primaryVariant
                )
            ) {

                SudokuBoard(
                    onEventHandler,
                    viewModel,
                    screenWidth - margin.dp
                )
            }

            Box(Modifier
                .wrapContentSize()
                .constrainAs(timer) {
                    top.linkTo(board.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 16.dp))
            {
                TimerText(viewModel)
            }

            Row(
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(diff) {
                        top.linkTo(board.bottom)
                        end.linkTo(parent.end)
                    }
            ) {
                (0..viewModel.difficulty.ordinal).forEach {
                    Icon(
                        contentDescription = stringResource(R.string.difficulty),
                        imageVector = Icons.Filled.Star,
                        tint = MaterialTheme.colors.secondary,
                        modifier = Modifier
                            .size(32.dp)
                            .padding(top = 4.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(inputs) {
                        top.linkTo(timer.bottom)
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (viewModel.boundary == 4) {
                    InputButtonRow(
                        (0..4).toList(),
                        onEventHandler
                    )
                } else {
                    InputButtonRow(
                        (0..4).toList(),
                        onEventHandler
                    )

                    InputButtonRow(
                        (5..9).toList(),
                        onEventHandler
                    )
                }
            }
        }
    }
}

@Composable
fun SudokuBoard(
    onEventHandler: (ActiveGameEvent) -> Unit,
    viewModel: ActiveGameViewModel,
    size: Dp
) {
    val boundary = viewModel.boundary

    val tileOffset = size.value / boundary

    var boardState by remember {
        mutableStateOf(viewModel.boardState, neverEqualPolicy())
    }

    viewModel.subBoardState = {
        boardState = it
    }

    SudokuTextFields(
        onEventHandler = onEventHandler,
        tileOffset = tileOffset,
        boardState = boardState
    )
    BoardGrid(boundary = boundary, tileOffset = tileOffset)
}

@Composable
fun SudokuTextFields(
    onEventHandler: (ActiveGameEvent) -> Unit,
    tileOffset: Float,
    boardState: HashMap<Int, SudokuTile>
) {
    boardState.values.forEach { tile ->
        var text = tile.value.toString()

        if (!tile.readOnly) {
            if (text == "0") text = ""

            Text(
                text = text,
                style = mutableSudokuSquare(tileOffset)
                    .copy(
                        color = if (MaterialTheme.colors.isLight) userInputtedNumberLight
                        else userInputtedNumberDark
                    ),
                modifier = Modifier
                    .absoluteOffset(
                        (tileOffset * (tile.x - 1)).dp,
                        (tileOffset * (tile.y - 1)).dp
                    )
                    .width(tileOffset.dp)
                    .height(tileOffset.dp)
                    .background(
                        if (tile.hasFocus) MaterialTheme.colors.onPrimary.copy(alpha = .25f)
                        else MaterialTheme.colors.surface
                    )
                    .clickable(onClick = {
                        onEventHandler.invoke(
                            ActiveGameEvent.OnTileFocused(tile.x, tile.y)
                        )
                    })
            )
        } else {
            Text(
                text = text,
                style = readOnlySudokuSquare(
                    tileOffset
                ),
                modifier = Modifier
                    .absoluteOffset(
                        (tileOffset * (tile.x - 1)).dp,
                        (tileOffset * (tile.y - 1)).dp
                    )
                    .width(tileOffset.dp)
                    .height(tileOffset.dp)
            )
        }
    }
}


@Composable
fun BoardGrid(boundary: Int, tileOffset: Float) {
    (1 until boundary).forEach {
        val width = if (it % boundary.sqrt == 0) 3.dp
        else 1.dp
        Divider(
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .absoluteOffset((tileOffset * it).dp, 0.dp)
                .fillMaxHeight()
                .width(width)
        )

        val height = if (it % boundary.sqrt == 0) 3.dp
        else 1.dp
        Divider(
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .absoluteOffset(0.dp, (tileOffset * it).dp)
                .fillMaxWidth()
                .height(height)
        )
    }
}


@Composable
fun TimerText(viewModel: ActiveGameViewModel) {

    var timerState by remember {
        mutableStateOf("")
    }

    viewModel.subTimerState = {
        timerState = it.toTime()
    }

    Text(
        modifier = Modifier.requiredHeight(36.dp),
        text = timerState,
        style = activeGameSubtitle.copy(color = MaterialTheme.colors.secondary)
    )
}

@Composable
fun InputButtonRow(
    numbers: List<Int>,
    onEventHandler: (ActiveGameEvent) -> Unit
) {
    Row {
        numbers.forEach {
            SudokuInputButton(
                onEventHandler,
                it
            )
        }
    }

    Spacer(modifier = Modifier.size(2.dp))
}

@Composable
fun SudokuInputButton(
    onEventHandler: (ActiveGameEvent) -> Unit,
    number: Int
) {
    TextButton(
        onClick = { onEventHandler.invoke(ActiveGameEvent.OnInput(number)) },
        modifier = Modifier
            .requiredSize(56.dp)
            .padding(2.dp),
        border = BorderStroke(ButtonDefaults.OutlinedBorderSize, MaterialTheme.colors.onPrimary),

        ) {
        Text(
            text = number.toString(),
            style = inputButton.copy(color = MaterialTheme.colors.onPrimary),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun GameCompleteContent(timerState: Long, isNewRecordState: Boolean) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                contentDescription = stringResource(R.string.game_complete),
                imageVector = Icons.Filled.EmojiEvents,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary),
                modifier = Modifier.size(128.dp, 128.dp)
            )

            if (isNewRecordState) Image(
                contentDescription = null,
                imageVector = Icons.Filled.Star,
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier
                    .size(36.dp, 36.dp)
                    .absoluteOffset(y = (-16).dp)
            )
        }

        Text(
            text = stringResource(R.string.total_time),
            style = newGameSubtitle.copy(
                color = MaterialTheme.colors.secondary
            )
        )

        Text(
            text = timerState.toTime(),
            style = newGameSubtitle.copy(
                color = MaterialTheme.colors.secondary,
                fontWeight = FontWeight.Normal
            )
        )
    }
}