package com.vocaengplus.vocaengplus.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: WordRepository,
) : ViewModel() {
    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _testTypes = listOf<String>("영어 문제 - 한국어 답", "한국어 문제 - 영어 답")
    val testTypes get() = _testTypes

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    private val _testWords = MutableStateFlow<List<Word>>(emptyList())
    val testWords: StateFlow<List<Word>> get() = _testWords

    private val _selectedWordListIndex = MutableStateFlow(0)
    val selectedWordListIndex: StateFlow<Int> get() = _selectedWordListIndex

    fun selectWordList(index: Int) {
        _selectedWordListIndex.value = index
    }

    fun getData() {
        viewModelScope.launch {
            getWordLists()
        }
    }

    private suspend fun getWordLists() {
        val names = repository.getWordLists()

        if (names.isSuccess) {
            names.getOrNull()?.let {
                _wordLists.value = it
            }
        } else {
            _snackBarMessage.value = "단어를 불러오는데 실패했습니다"
        }
    }

    fun getWords() {
        viewModelScope.launch {
            val wordListUid = getWordListUidByIndex()

            val testWords = repository.getWords(wordListUid)
            if (testWords.isSuccess) {
                testWords.getOrNull()?.let {
                    _testWords.value = it.shuffled().subList(0, 10)
                }
            } else {
                _snackBarMessage.value = "단어를 불러오는데 실패했습니다"
            }
        }

    }


    private fun getWordListUidByIndex(): String {
        //TODO index값이 범위를 벗어나면 오류
        return _wordLists.value[_selectedWordListIndex.value].wordListUid
    }

}