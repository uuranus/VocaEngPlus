package com.vocaengplus.vocaengplus.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.data.WordList
import com.vocaengplus.vocaengplus.model.repository.SearchRepository
import com.vocaengplus.vocaengplus.model.repository.WordRepository
import com.vocaengplus.vocaengplus.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _isKorToEng = MutableStateFlow(true)
    val isKorToEng: StateFlow<Boolean> get() = _isKorToEng

    val inputText = MutableStateFlow("")

    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> get() = _translatedText

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    private val _currentWordList = MutableStateFlow(0)
    val currentWordList: StateFlow<Int> get() = _currentWordList

    private val _isSavePossible = MutableStateFlow(false)
    val isSavePossible: StateFlow<Boolean> get() = _isSaveSuccess

    private val _isSaveSuccess = MutableStateFlow(false)
    val isSaveSuccess: StateFlow<Boolean> get() = _isSaveSuccess
    fun switch() {
        _isKorToEng.value = _isKorToEng.value.not()
    }

    fun getCurrentWord(): Word {
        return if (_isKorToEng.value) {
            Word("", translatedText.value, inputText.value)
        } else {
            Word("", inputText.value, translatedText.value)
        }
    }

    fun selectWordList(index: Int) {
        _currentWordList.value = index
    }

    fun translate() {
        val translateText = inputText.value
        println("translateText $translateText")

        viewModelScope.launch {
            val translatedResponse = if (_isKorToEng.value) {
                searchRepository.translate(KO, EN, translateText)
            } else {
                searchRepository.translate(EN, KO, translateText)
            }

            translatedResponse.getOrNull()?.let {
                _translatedText.value = it
            }
        }

    }

    fun getWordLists() {
        viewModelScope.launch {
            val wordListsResponse = wordRepository.getWordLists()
            if (wordListsResponse.isSuccess) {
                wordListsResponse.getOrNull()?.let {
                    _wordLists.value = it
                }
            } else {
                _snackBarMessage.value = "저장할 수 있는 단어장이 없습니다."
                _isSavePossible.value = false
            }
        }
    }

    fun saveWord() {
        if (_isKorToEng.value) {
            isValidateWord(translatedText.value)
            isValidateMeaning(inputText.value)
        } else {
            isValidateWord(inputText.value)
            isValidateMeaning(translatedText.value)
        }
        if (_isSavePossible.value.not()) return

        viewModelScope.launch {

            val wordListUid = _wordLists.value[_currentWordList.value].wordListUid
            val newWord = if (_isKorToEng.value) {
                Word(wordListUid, translatedText.value, inputText.value)
            } else {
                Word(wordListUid, inputText.value, translatedText.value)
            }

            val addWordResponse = wordRepository.addOneWord(wordListUid, newWord)
            if (addWordResponse.isSuccess) {
                _snackBarMessage.value = "단어를 추가했습니다"
            } else {
                _snackBarMessage.value = "단어 추가에 실패했습니다."
            }
        }
    }

    private fun isValidateWord(word: String) {
        _isSavePossible.value = if (Validation.isValidateWord(word)) {
            true
        } else {
            println("word not valid")
            _snackBarMessage.value = "영단어가 올바르지 않아 저장할 수 없습니다."
            false
        }
    }

    private fun isValidateMeaning(meaning: String) {
        _isSavePossible.value = if (Validation.isValidateMeaning(meaning)) {
            true
        } else {
            _snackBarMessage.value = "뜻이 올바르지 않아 저장할 수 없습니다."
            false
        }
    }

    companion object {
        const val KO = "ko"
        const val EN = "en"
    }
}
