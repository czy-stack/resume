package com.example.jetpack.ui.activity.word

import androidx.lifecycle.*
import com.example.jetpack.repository.inDb.WordRepository
import com.example.jetpack.vo.Word
import kotlinx.coroutines.launch

/**
 * @作者 陈忠岳
 * @主要功能
 * @创建日期  2022/9/22
 */
class WordViewModel(private val repository: WordRepository) : ViewModel() {

    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}