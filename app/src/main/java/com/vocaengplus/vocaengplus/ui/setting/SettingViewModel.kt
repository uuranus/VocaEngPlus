package com.vocaengplus.vocaengplus.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.UserRepository
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import com.vocaengplus.vocaengplus.ui.util.Validation
import com.vocaengplus.vocaengplus.ui.util.getToday
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: WordRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _snackBarMessage = MutableStateFlow<String>("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    val newWordListName = MutableStateFlow("")

    private val _writerName = MutableStateFlow("")
    val writeName: StateFlow<String> get() = _writerName

    private val _addDate = MutableStateFlow("")
    val addDate: StateFlow<String> get() = _addDate

    val newWordListDescription = MutableStateFlow("")

    private val _newWordLists = MutableStateFlow<MutableList<Word>>(mutableListOf())
    val newWordLists: StateFlow<MutableList<Word>> get() = _newWordLists

    private val _isSavedSuccess = MutableStateFlow(false)
    val isSavedSuccess: StateFlow<Boolean> get() = _isSavedSuccess

    fun getNewCategoryWriterAndDate() {
        viewModelScope.launch {
            val name = userRepository.getUserName()
            _writerName.value = name
        }
        _addDate.value = getToday()
    }

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

    fun addNewWord(word: String, meaning: String) {
        if (word.isEmpty() || meaning.isEmpty()) {
            _snackBarMessage.value = "단어 추가 실패"
        } else {
            if (Validation.isValidateWord(word).not()) {
                _snackBarMessage.value = "영단어 입력이 올바르지 않아 추가에 실패하였습니다."
            } else if (Validation.isValidateMeaning(meaning).not()) {
                _snackBarMessage.value = "뜻 입력이 올바르지 않아 추가에 실패하였습니다."
            } else {
                _newWordLists.value.add(Word(word, meaning, false, ""))
            }
        }
    }

    fun removeNewWord(position: Int) {
        _newWordLists.value.removeAt(position)
    }

    fun addNewWordList() {
        if (newWordListDescription.value.isEmpty()) {
            _snackBarMessage.value = "단어장 이름을 입력해주세요"
            return
        }
        if (newWordListName.value.isEmpty()) {
            _snackBarMessage.value = "내용을 입력해주세요"
            return
        }
        if (_newWordLists.value.isEmpty()) {
            _snackBarMessage.value = "단어가 최소 한 개 이상 존재해야 합니다"
            return
        }

        viewModelScope.launch {
            val newWordList = WordList(
                newWordListName.value,
                "",
                "",
                System.currentTimeMillis(),
                newWordListDescription.value,
                _newWordLists.value.map { it.copy(wordListName = newWordListName.value) }
            )
            val addNewWordListResponse = repository.addNewWordList(newWordList)
            if (addNewWordListResponse.isSuccess) {
                _isSavedSuccess.value = true
            } else {
                _snackBarMessage.value = "단어장을 추가하는데 실패했습니다"
            }
        }
    }
}