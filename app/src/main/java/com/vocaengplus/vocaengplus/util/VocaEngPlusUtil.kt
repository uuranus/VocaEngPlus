package com.vocaengplus.vocaengplus.util

import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.data.WordList

enum class WORDLIST {
    TOEIC,
    TOFLE,
    SOONEUNG
}

fun makeDefaultWordList(type: WORDLIST, name: String, uid: String): WordList {
    return when (type) {
        WORDLIST.TOEIC -> {
            WordList(
                "", "토익 영단어", name, uid, System.currentTimeMillis(),
                "토익 필수 영단어",
            )
        }

        WORDLIST.TOFLE -> {
            WordList(
                "", "토플 영단어", name, uid, System.currentTimeMillis(),
                "토플 필수 영단어"
            )
        }

        WORDLIST.SOONEUNG -> {
            WordList(
                "", "수능 영단어", name, uid, System.currentTimeMillis(),
                "수능 필수 영단어",
            )
        }
    }
}

fun makeDefaultWords(type: WORDLIST, wordListUid: String): List<Word> {

    return when (type) {
        WORDLIST.TOEIC -> {
            listOf(
                Word(wordListUid, "agreement", "계약서", false),
                Word(wordListUid, "beverage", "음료", false),
                Word(wordListUid, "carve", "조각하다", false),
                Word(wordListUid, "clear off", "치우다", false),
                Word(wordListUid, "gather", "모이다", false),
                Word(wordListUid, "lift", "들다", false),
                Word(wordListUid, "neighborhood", "동네", false),
                Word(wordListUid, "or so", "정도,쯤", false),
                Word(wordListUid, "remove", "벗다", false),
                Word(wordListUid, "shine", "비추다", false),
            )
        }

        WORDLIST.TOFLE -> {
            listOf(
                Word(wordListUid, "abrupt", "갑작스러운", false),
                Word(wordListUid, "bound", "경계", false),
                Word(wordListUid, "constitute", "구성하다", false),
                Word(wordListUid, "flee", "피하다", false),
                Word(wordListUid, "hazardous", "위험한", false),
                Word(wordListUid, "link", "결합", false),
                Word(wordListUid, "principle", "원칙", false),
                Word(wordListUid, "scold", "꾸짖다", false),
                Word(wordListUid, "suspend", "연기하다", false),
                Word(wordListUid, "withhold", "보류하다", false),
            )
        }

        WORDLIST.SOONEUNG -> {
            listOf(
                Word(wordListUid, "absence", "결석,부재", false),
                Word(wordListUid, "accounting", "회계", false),
                Word(wordListUid, "application", "응용", false),
                Word(wordListUid, "candidate", "후보자", false),
                Word(wordListUid, "delay", "지연시키다", false),
                Word(wordListUid, "deserve", "받을 자격(가치)가 있다", false),
                Word(wordListUid, "flexible", "유연한,신축성 있는", false),
                Word(wordListUid, "gratitude", "감사,고마움", false),
                Word(wordListUid, "notepad", "메모지", false),
                Word(wordListUid, "surface", "표면", false),
            )
        }
    }
}