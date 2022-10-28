package com.example.jetpack.ui.activity.word

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.common.utils.toast
import com.example.jetpack.R
import com.example.jetpack.WordsApplication
import com.example.jetpack.adapter.WordListAdapter
import com.example.jetpack.db.WordRoomDatabase
import com.example.jetpack.repository.inDb.WordRepository
import com.example.jetpack.ui.activity.new.NewWordActivity
import com.example.jetpack.vo.Word
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.reflect.KProperty

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/22
 */
class WordActivity : AppCompatActivity(){
    private val wordViewModel : WordViewModel by viewModels{
        WordViewModelFactory((application as WordsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = WordListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        wordViewModel.allWords.observe(this) { words ->
            words?.let { adapter.submitList(it) }
        }

        val  registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode != Activity.RESULT_OK) {
                this.toast(R.string.empty_not_saved)
                return@registerForActivityResult
            }
            it.data?.extras?.getString(NewWordActivity.EXTRA_REPLY)?.let { word ->
                wordViewModel.insert(Word(word))
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            registerForActivityResult.launch(Intent(this,NewWordActivity::class.java))
        }
    }
}
