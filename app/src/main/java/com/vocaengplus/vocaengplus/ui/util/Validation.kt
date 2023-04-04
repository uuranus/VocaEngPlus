package com.vocaengplus.vocaengplus.ui.util

object Validation {

    fun checkInput(input: Array<String>): Boolean {
        var flag = true
        input.forEach {
            if (it.isEmpty()) {
                flag = false
                return@forEach
            }
        }
        return flag
    }

    fun isValidateEmail(email: String): Boolean {
        val emailValidation =
            """^[a-zA-Z0-9]([-_.]?[a-zA-Z0-9])*@[a-zA-Z0-9]([-_.]?[a-zA-Z0-9])*\.[a-zA-Z0-9]{2,3}$""".toRegex()
        if (emailValidation.matches(email)) {
            return true
        }
        return false
    }

    fun isValidateNickname(nickname: String): Boolean {
        val nicknameValidation = """^[a-zA-Z0-9가-힣]{2,10}$""".toRegex()
        if (nicknameValidation.matches(nickname)) {
            return true
        }
        return false
    }

    fun isValidatePassword(password: String): Boolean {
        val passwordValidation = """^.{6,20}$""".toRegex()
        if (passwordValidation.matches(password)) {
            return true
        }
        return false
    }

    fun isValidateWord(word: String): Boolean {
        val wordValidation = "^[a-zA-Z0-9][^가-힣@#$%^&*+/><=\\[\\].\\\\]{1,39}$".toRegex()
        if (wordValidation.matches(word)) {
            return true
        }
        return false
    }

    fun isValidateMeaning(meaning: String): Boolean {
        val meaningValidation = "^[가-힣0-9][^a-zA-Z@#$%^&*+/><=\\\\\\[\\]]{0,39}$".toRegex()
        if (meaningValidation.matches(meaning)) {
            return true
        }
        return false
    }

    fun isValidateCategoryName(category: String): Boolean {
        val categoryValidation = "^[가-힣A-Za-z0-9][^.#$\\[\\]]{1,19}$".toRegex()
        if (categoryValidation.matches(category)) {
            return true
        }
        return false
    }


}