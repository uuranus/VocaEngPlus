package com.vocaengplus.vocaengplus.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: WordRepository,
) : ViewModel() {

    private val _snackBarMessage = MutableStateFlow<String>("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    private val _currentSelectedWordList = MutableStateFlow<Int>(0)
    val currentSelectedWordList: StateFlow<Int> get() = _currentSelectedWordList

    fun selectWordList(position: Int) {
        _currentSelectedWordList.value = position
    }

    fun getSelectedWordList(): WordList = _wordLists.value[_currentSelectedWordList.value]

    fun getAllWordLists() {
        viewModelScope.launch {
            val wordLists = repository.getWordLists()
            if (wordLists.isSuccess) {
                wordLists.getOrNull()?.let {
                    _wordLists.value = it
                }
            } else {
                _snackBarMessage.value = "단어장이 없습니다"
            }
        }
    }

    fun editWordList(newName: String, newDescription: String) {
        if (isValidWordList(newName, newDescription).not()) return

        viewModelScope.launch {
            val wordList = _wordLists.value[_currentSelectedWordList.value].copy(
                wordListName = newName,
                description = newDescription,
            )
            val editResponse = repository.editWordList(wordList)
            if (editResponse.isSuccess) {
                _wordLists.value = _wordLists.value.mapIndexed { index, oldWordList ->
                    if (index == _currentSelectedWordList.value) {
                        wordList
                    } else {
                        oldWordList
                    }
                }
            } else {
                _snackBarMessage.value = "단어장 수정에 실패했습니다."
            }
        }
    }

    private fun isValidWordList(newName: String, newDescription: String): Boolean {
        if (!(Validation.checkInput(arrayOf(newName)))) {
            _snackBarMessage.value = "단어장 이름을 입력해주세요"
            return false
        }

        if (!(Validation.isValidateCategoryName(newName))) {
            _snackBarMessage.value = "단어장 이름은 1~20글자여야 합니다.(.#$[] 제외)"
            return false
        }

        if (!(Validation.checkInput(arrayOf(newDescription)))) {
            _snackBarMessage.value = "단어장 내용을 입력해주세요"
            return false
        }

        if (newDescription.length > 40) {
            _snackBarMessage.value = "단어장 내용은 1~40자 이내여야 합니다"
            return false
        }
        return true
    }

    fun deleteWordList() {
        viewModelScope.launch {
            val deleteWordListUid = _wordLists.value[_currentSelectedWordList.value].wordListUid
            val deleteResponse =
                repository.deleteWordList(deleteWordListUid)
            if (deleteResponse.isSuccess) {
                _wordLists.value = _wordLists.value.filter {
                    it.wordListUid != deleteWordListUid
                }
            } else {
                _snackBarMessage.value = "단어장 삭제에 실패했습니다."
            }
        }
    }
}