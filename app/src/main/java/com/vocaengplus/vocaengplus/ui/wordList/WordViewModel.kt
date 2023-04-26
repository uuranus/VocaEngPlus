package com.vocaengplus.vocaengplus.ui.wordList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.Voca
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

    private val _wordList: MutableStateFlow<List<Voca>> = MutableStateFlow(emptyList())
    val wordList: StateFlow<List<Voca>> get() = _wordList

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

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

    fun setMyWord(voca: Voca, isSelected: Int) {
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

        fun getData() {

            viewModelScope.launch {
                val wordListUid = ""
                repository.getWordList(wordListUid)
            }
        }
    }
}