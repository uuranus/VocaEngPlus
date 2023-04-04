package com.vocaengplus.vocaengplus.ui.util

import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.model.data.newData.WordList

enum class WORDLIST {
    TOEIC,
    TOFLE,
    SOONEUNG
}

fun makeDefaultWordList(type: WORDLIST, name: String, uid: String): WordList {

    return when (type) {
        WORDLIST.TOEIC -> {
            WordList(
                "토익 영단어", name, uid, System.currentTimeMillis(),
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
        }
        WORDLIST.TOFLE -> {
            WordList(
                "토플 영단어", name, uid, System.currentTimeMillis(),
                "토플 필수 영단어",
                listOf(
                    Word("abrupt", "갑작스러운", false, "토플 영단어"),
                    Word("bound", "경계", false, "수능 영단어"),
                    Word("constitute", "구성하다", false, "수능 영단어"),
                    Word("flee", "피하다", false, "수능 영단어"),
                    Word("hazardous", "위험한", false, "수능 영단어"),
                    Word("link", "결합", false, "수능 영단어"),
                    Word("principle", "원칙", false, "수능 영단어"),
                    Word("scold", "꾸짖다", false, "수능 영단어"),
                    Word("suspend", "연기하다", false, "수능 영단어"),
                    Word("withhold", "보류하다", false, "수능 영단어"),
                )
            )
        }
        WORDLIST.SOONEUNG -> {
            WordList(
                "수능 영단어", name, uid, System.currentTimeMillis(),
                "수능 필수 영단어",
                listOf(
                    Word("absence", "결석,부재", false, "수능 영단어"),
                    Word("accounting", "회계", false, "수능 영단어"),
                    Word("application", "응용", false, "수능 영단어"),
                    Word("candidate", "후보자", false, "수능 영단어"),
                    Word("delay", "지연시키다", false, "수능 영단어"),
                    Word("deserve", "받을 자격(가치)가 있다", false, "수능 영단어"),
                    Word("flexible", "유연한,신축성 있는", false, "수능 영단어"),
                    Word("gratitude", "감사,고마움", false, "수능 영단어"),
                    Word("notepad", "메모지", false, "수능 영단어"),
                    Word("surface", "표면", false, "수능 영단어"),
                )
            )
        }
    }
}