package com.vocaengplus.vocaengplus.ui.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.LogRepository
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val logRepository: LogRepository,
    private val wordRepository: WordRepository,
) : ViewModel() {
    private val _snackBarMessage = MutableStateFlow<String>("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _currentBlackBox = MutableStateFlow(Word("", "", ""))
    val currentBlackBox: StateFlow<Word> get() = _currentBlackBox

    private val _currentWordListInfo = MutableStateFlow(WordList())
    val currentWordListInfo: StateFlow<WordList> get() = _currentWordListInfo

    private val _reviewWords = MutableStateFlow<List<Word>>(emptyList())
    val reviewWords: StateFlow<List<Word>> get() = _reviewWords

    private val _isMeaningShowed = MutableStateFlow(false)
    val isMeaningShowed: StateFlow<Boolean> get() = _isMeaningShowed

    fun resetBlackBox() {
        _isMeaningShowed.value = false
    }

    fun showBlackBox(){
        _isMeaningShowed.value = true
    }

    fun setBlackBox(index: Int) {
        _currentBlackBox.value = _reviewWords.value[index]
    }

    fun getWordListInfo(index: Int) {
        viewModelScope.launch {
            val wordListUid = _reviewWords.value[index].wordListUid
            val networkResponse = wordRepository.getWordList(wordListUid)

            if (networkResponse.isSuccess) {
                networkResponse.getOrNull()?.let {
                    _currentWordListInfo.value = it
                }
            } else {
                _snackBarMessage.value = "단어장 정보를 가져오는데 실패했습니다"
            }
        }
    }

    fun getReviews() {
        viewModelScope.launch {
            val networkResponse = logRepository.getReviews()

            if (networkResponse.isSuccess) {
                networkResponse.getOrNull()?.let {
                    _reviewWords.value = it
                }
            } else {
                _snackBarMessage.value = "리뷰할 단어가 없습니다"
            }
        }
    }

}