package com.vocaengplus.vocaengplus.ui.myWord

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyWordViewModel @Inject constructor(
    private val repository: WordRepository,
) : ViewModel() {

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _currentWords = MutableStateFlow<List<Word>>(emptyList())
    val currentWords: StateFlow<List<Word>> get() = _currentWords

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    private val _selectedWordListIndex = MutableStateFlow(0)
    val selectedWordListIndex: StateFlow<Int> get() = _selectedWordListIndex

    private val _selectedWordIndex = MutableStateFlow(0)
    val selectedWordIndex: StateFlow<Int> get() = _selectedWordIndex

    fun selectWordList(index: Int) {
        viewModelScope.launch {
            _selectedWordListIndex.value = index
            getWords()
        }
    }

    fun selectWord(index: Int) {
        _selectedWordIndex.value = index
    }

    fun getSelectedWord(): Word {
        return _currentWords.value[_selectedWordIndex.value]
    }

    fun getData() {
        viewModelScope.launch {
            getWordLists()
            getWords()
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

    private suspend fun getWords() {
        val wordListUid = getWordListUidByIndex()

        val currentWords = repository.getMyWords(wordListUid)
        if (currentWords.isSuccess) {
            currentWords.getOrNull()?.let {
                _currentWords.value = it
            }
        } else {
            _snackBarMessage.value = "단어장이 비었습니다"
        }
    }

    private fun getWordListUidByIndex(): String {
        //TODO index값이 범위를 벗어나면 오류
        return if (_currentWords.value.isEmpty()) {
            _wordLists.value[_selectedWordListIndex.value].wordListUid
        } else {
            _currentWords.value[_selectedWordIndex.value].wordListUid
        }
    }

    fun setMyWord(position: Int) {
        val words = _currentWords.value.mapIndexed { index, word ->
            if (index == position) {
                word.copy(checked = word.checked.not())
            } else {
                word
            }
        }
        val wordLisUid = getWordListUidByIndex()

        viewModelScope.launch {
            val checkedResponse = repository.editWord(wordLisUid, words)
            if (checkedResponse.isSuccess) {
                _currentWords.value = words
            } else {
                _snackBarMessage.value = "즐겨찾기 추가에 실패했습니다"
            }
        }
    }

    fun editWord(word: Word) {
        if (isValidWord(word.word, word.meaning).not()) return
        val wordLisUid = getWordListUidByIndex()
        val words = _currentWords.value.mapIndexed { index, oldWord ->
            if (index == _selectedWordIndex.value) {
                word
            } else {
                oldWord
            }
        }

        viewModelScope.launch {
            val editResult = repository.editWord(wordLisUid, words)
            if (editResult.isSuccess) {
                _currentWords.value = words
            } else {
                _snackBarMessage.value = "단어 수정에 실패했습니다"
            }
//                    repository.setLog()
        }
    }

    private fun isValidWord(word: String, meaning: String): Boolean {
        return if (word.isEmpty() || meaning.isEmpty()) {
            _snackBarMessage.value = "단어 추가 실패"
            false
        } else if (Validation.isValidateWord(word).not()) {
            _snackBarMessage.value = "영단어 입력이 올바르지 않아 추가에 실패하였습니다."
            false
        } else if (Validation.isValidateMeaning(meaning).not()) {
            _snackBarMessage.value = "뜻 입력이 올바르지 않아 추가에 실패하였습니다."
            false
        } else {
            true
        }
    }

    fun deleteWord(word: Word) {
        val wordLisUid = getWordListUidByIndex()
        val words = _currentWords.value.filter {
            it != word
        }
        viewModelScope.launch {
            val deleteResult = repository.editWord(wordLisUid, words)
            if (deleteResult.isSuccess) {
                _currentWords.value = words
            } else {
                _snackBarMessage.value = "단어 삭제에 실패했습니다"
            }
//                    repository.setLog()
        }

    }

}