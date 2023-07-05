package com.vocaengplus.vocaengplus.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.model.data.repository.WordRepository
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

    fun getAllWordLists() {
        viewModelScope.launch {
            println("getAllWordLists!!")
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
}