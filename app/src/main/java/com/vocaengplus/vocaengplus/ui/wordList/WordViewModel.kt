package com.vocaengplus.vocaengplus.ui.wordList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.Voca
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
class WordViewModel @Inject constructor(
    private val repository: WordRepository,
) : ViewModel() {

    private val _wordsData = mutableListOf<WordList>(
        WordList(
            "토익 영단어", "", "uid", System.currentTimeMillis(),
            "토익 필수 영단어",
            listOf(
                Word("agreement", "계약서", false, "토익 영단어"),
                Word("beverage", "음료", false, "토익 영단어"),
                Word("carve", "조각하다", false, "토익 영단어"),
                Word("clear off", "치우다", false, "토익 영단어"),
                Word("gather", "모이다", false, "토익 영단어"),
                Word("lift", "들다", false, "토익 영단어"),
                Word("neighborhood", "동네", false, "토익 영단어"),
                Word("or so", "정도,쯤", false, "토익 영단어"),
                Word("remove", "벗다", false, "토익 영단어"),
                Word("shine", "비추다", false, "토익 영단어"),
            )
        )
    )

    private val _selectedWordListIndex = MutableStateFlow(0)
    val selectedWordListIndex: StateFlow<Int> get() = _selectedWordListIndex

    private val _wordListNames: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val wordListNames: StateFlow<List<String>> get() = _wordListNames

    private val _wordListUids: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val wordListUids: StateFlow<List<String>> get() = _wordListUids

    private val _currentWordList: MutableStateFlow<WordList> =
        MutableStateFlow(WordList())
    val currentWordList: StateFlow<WordList> get() = _currentWordList

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    fun selectWordList(index: Int) {
        viewModelScope.launch {
            _selectedWordListIndex.value = index
            getWordListNames()
        }
    }

    fun getData() {
        viewModelScope.launch {
            getWordListNames()
            getWordList()
        }
    }

    private suspend fun getWordListNames() {
        val names = repository.getWordListNames()

        if (names.isSuccess) {
            names.getOrNull()?.let {
                _wordListNames.value = it.map { it2 -> it2.name }
                _wordListUids.value = it.map { it2 -> it2.uid }
            }
        } else {
            _snackBarMessage.value = "단어를 불러오는데 실패했습니다"
        }
    }

    private suspend fun getWordList() {
        val wordListUid = getWordListUidByIndex()
        if (wordListUid.isEmpty()) return

        val currentWordList = repository.getWordList(wordListUid)
        if (currentWordList.isSuccess) {
            currentWordList.getOrNull()?.let {
                _currentWordList.value = it
            }
        } else {
            _snackBarMessage.value = "단어장이 비었습니다"
        }
    }

    private fun getWordListUidByIndex(): String {
        if (_wordListUids.value.isEmpty()) return ""
        _selectedWordListIndex.value =
            _selectedWordListIndex.value.coerceAtMost(_wordListUids.value.size)
        return _wordListUids.value[_selectedWordListIndex.value]
    }

    fun addNewVoca(word: String, meaning: String) {
        if (word.isEmpty() || meaning.isEmpty()) {
            _snackBarMessage.value = "단어 추가 실패"
        } else {
            if (Validation.isValidateWord(word).not()) {
                _snackBarMessage.value = "영단어 입력이 올바르지 않아 추가에 실패하였습니다."
            } else if (Validation.isValidateMeaning(meaning).not()) {
                _snackBarMessage.value = "뜻 입력이 올바르지 않아 추가에 실패하였습니다."
            } else {
                viewModelScope.launch {
                    val newVoca = Voca("", word, meaning, 0)
                    repository.saveNewWord(newVoca)

//                    repository.setLog()
                }
            }

        }
    }

    fun setMyWord(word: Word, isSelected: Boolean) {
//        repository.setMyWord(voca.copy(checked = isSelected))
    }

    fun editVoca(voca: Voca) {
        if (voca.word.isEmpty() || voca.meaning.isEmpty()) {
            _snackBarMessage.value = "단어를 입력하세요"
            return
        }
        if (Validation.isValidateWord(voca.word).not()) {
            _snackBarMessage.value = "영단어 입력이 올바르지 않습니다."
        } else if (Validation.isValidateMeaning(voca.meaning).not()) {
            _snackBarMessage.value = "뜻 입력이 올바르지 않습니다."
        } else {
            viewModelScope.launch {
                repository.saveEditedVoca(voca)
//                    repository.setLog()
            }
        }
    }
}