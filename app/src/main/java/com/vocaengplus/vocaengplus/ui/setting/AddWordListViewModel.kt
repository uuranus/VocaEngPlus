package com.vocaengplus.vocaengplus.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.UserRepository
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import com.vocaengplus.vocaengplus.util.Validation
import com.vocaengplus.vocaengplus.util.getToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddWordListViewModel @Inject constructor(
    private val repository: WordRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _snackBarMessage = MutableStateFlow<String>("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _isSavedSuccess = MutableStateFlow(false)
    val isSavedSuccess: StateFlow<Boolean> get() = _isSavedSuccess

    private val _writerName = MutableStateFlow("")
    val writeName: StateFlow<String> get() = _writerName

    private val _addDate = MutableStateFlow("")
    val addDate: StateFlow<String> get() = _addDate

    val newWordListName = MutableStateFlow("")

    val newWordListDescription = MutableStateFlow("")

    private val _newWordLists = MutableStateFlow<MutableList<Word>>(mutableListOf())
    val newWordLists: StateFlow<MutableList<Word>> get() = _newWordLists

    fun getWriterAndDate() {
        viewModelScope.launch {
            val name = userRepository.getUserName()
            _writerName.value = name
        }
        _addDate.value = getToday()
    }

    fun addNewWord(word: Word) {
        if (isValidWord(word).not()) return
        _newWordLists.value.add(word)
    }

    private fun isValidWord(word: Word): Boolean {
        return if (word.word.isEmpty() || word.meaning.isEmpty()) {
            _snackBarMessage.value = "단어 추가 실패"
            false
        } else if (Validation.isValidateWord(word.word).not()) {
            _snackBarMessage.value = "영단어 입력이 올바르지 않아 추가에 실패하였습니다."
            false
        } else if (Validation.isValidateMeaning(word.meaning).not()) {
            _snackBarMessage.value = "뜻 입력이 올바르지 않아 추가에 실패하였습니다."
            false
        } else {
            true
        }
    }

    fun removeNewWord(position: Int) {
        _newWordLists.value =
            _newWordLists.value.filterIndexed { index, word -> index != position }
                .toMutableList()
    }

    fun addNewWordList() {
        if (isValidWordList().not()) return

        viewModelScope.launch {
            val newWordList = WordList(
                "",
                newWordListName.value,
                "",
                "",
                System.currentTimeMillis(),
                newWordListDescription.value
            )
            val newWords =
                _newWordLists.value
            val addNewWordListResponse =
                repository.addWordList(newWordList, words = newWords)
            if (addNewWordListResponse.isSuccess) {
                _isSavedSuccess.value = true
            } else {
                _snackBarMessage.value = "단어장을 추가하는데 실패했습니다"
            }
        }
    }

    private fun isValidWordList(): Boolean {
        return if (newWordListDescription.value.isEmpty()) {
            _snackBarMessage.value = "단어장 이름을 입력해주세요"
            false
        } else if (newWordListName.value.isEmpty()) {
            _snackBarMessage.value = "내용을 입력해주세요"
            false
        } else if (_newWordLists.value.isEmpty()) {
            _snackBarMessage.value = "단어가 최소 한 개 이상 존재해야 합니다"
            false
        } else {
            true
        }
    }
}