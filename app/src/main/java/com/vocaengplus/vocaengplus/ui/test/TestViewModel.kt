package com.vocaengplus.vocaengplus.ui.test

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.Test
import com.vocaengplus.vocaengplus.model.data.newData.TestResult
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

    private val _testTypes = listOf("영어 문제 - 한국어 답", "한국어 문제 - 영어 답")
    val testTypes get() = _testTypes

    private val _testTypesIndex = MutableStateFlow(0)
    val testTypesIndex get() = _testTypesIndex

    private val _selectedWordListIndex = MutableStateFlow(0)
    val selectedWordListIndex: StateFlow<Int> get() = _selectedWordListIndex

    private val _wordLists = MutableStateFlow<List<WordList>>(emptyList())
    val wordLists: StateFlow<List<WordList>> get() = _wordLists

    private val _testWords = MutableStateFlow<List<Word>>(emptyList())
    val testWords: StateFlow<List<Word>> get() = _testWords

    private val _currentTestWord = MutableStateFlow(Test("", listOf("", "", "", ""), ""))
    val currentTestWord: StateFlow<Test> get() = _currentTestWord

    private val _testResults = MutableStateFlow<MutableList<TestResult>>(mutableListOf())
    val testResults: StateFlow<List<TestResult>> get() = _testResults

    private val _moveToTest = MutableStateFlow(false)
    val moveToTest: StateFlow<Boolean> get() = _moveToTest

    private val _moveToResult = MutableStateFlow(false)
    val moveToResult: StateFlow<Boolean> get() = _moveToResult

    fun selectWordList(index: Int) {
        _selectedWordListIndex.value = index
    }

    fun selectTestType(index: Int) {
        _testTypesIndex.value = index
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
                    if (it.size < 4) {
                        _snackBarMessage.value = "테스트를 진행하기에 단어가 부족합니다"
                    } else {
                        val size = if (it.size < 10) it.size else 10
                        _testWords.value = it.shuffled().subList(0, size)
                        _moveToTest.value = true
                    }
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

    fun getTest() {
        if (_testResults.value.size == _testWords.value.size) {
            _moveToResult.value = true
            return
        }

        _currentTestWord.value = if (_testTypesIndex.value == 0) {
            getEnglishKoreanTest()
        } else {
            getKoreanEnglishTest()
        }
    }

    private fun getEnglishKoreanTest(): Test {
        val cur = _testWords.value[_testResults.value.size]
        return Test(
            cur.word,
            getRandomInCorrectKoreanAnswers(cur.meaning).plus(cur.meaning).shuffled(),
            cur.meaning
        )
    }

    private fun getKoreanEnglishTest(): Test {
        val cur = _testWords.value[_testResults.value.size]
        return Test(
            cur.meaning,
            getRandomInCorrectEnglishAnswers(cur.word).plus(cur.word).shuffled(),
            cur.word
        )
    }

    private fun getRandomInCorrectEnglishAnswers(answer: String): List<String> =
        _testWords.value.filter { it.word != answer }.map { it.word }.shuffled().subList(0, 3)


    private fun getRandomInCorrectKoreanAnswers(answer: String): List<String> =
        _testWords.value.filter { it.meaning != answer }.map { it.meaning }.shuffled().subList(0, 3)

    fun checkMyAnswer(position: Int) {
        if (_currentTestWord.value.answers[position] == _currentTestWord.value.answer) {
            _snackBarMessage.value = "맞았습니다!"
            _testResults.value.add(
                TestResult(
                    _currentTestWord.value.question,
                    _currentTestWord.value.answer,
                    true
                )
            )
        } else {
            _snackBarMessage.value = "틀렸습니다"
            _testResults.value.add(
                TestResult(
                    _currentTestWord.value.question,
                    _currentTestWord.value.answer,
                    false
                )
            )
        }
        _snackBarMessage.value = ""

        getTest()
    }
}
