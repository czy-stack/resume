package com.example.sudo.ui.newgame

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.sudo.R
import com.example.sudo.common.makeToast
import com.example.sudo.ui.GraphSudokuTheme
import com.example.sudo.ui.activegame.ActiveGameActivity
import com.example.sudo.ui.newgame.buildlogic.buildNewGameLogic

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/10/14
 */
class NewGameActivity : AppCompatActivity(),NewGameContainer {
    private lateinit var logic: NewGameLogic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = NewGameViewModel()

        setContent {
            GraphSudokuTheme {
                NewGameScreen(onEventHandler = logic::onEvent, viewModel = viewModel)
            }
        }
        logic = buildNewGameLogic(this,viewModel,applicationContext)
    }

    override fun showError() = makeToast(getString(R.string.generic_error))

    override fun onStart() {
        super.onStart()
        logic.onEvent(NewGameEvent.OnStart)
    }

    override fun onDoneClick() {
        startActiveGameActivity()
    }

    override fun onBackPressed() {
        startActiveGameActivity()
    }

    private fun startActiveGameActivity(){
        startActivity(Intent(this,ActiveGameActivity::class.java).apply { this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK})
    }


}