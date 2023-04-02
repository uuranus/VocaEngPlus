package com.vocaengplus.vocaengplus.ui.login

import androidx.lifecycle.ViewModel
import com.vocaengplus.vocaengplus.ui.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordCheck = MutableStateFlow("")
    val nickname = MutableStateFlow("")

    private val _emailErrorText = MutableStateFlow("")
    val emailErrorText: StateFlow<String> get() = _emailErrorText

    private val _passwordErrorText = MutableStateFlow("")
    val passwordErrorText: StateFlow<String> get() = _passwordErrorText

    private val _passwordCheckErrorText = MutableStateFlow("")
    val passwordCheckErrorText: StateFlow<String> get() = _passwordCheckErrorText

    private val _nicknameErrorText = MutableStateFlow("")
    val nicknameErrorText: StateFlow<String> get() = _nicknameErrorText

    fun onEmailChanged() {
        val emailValue = email.value

        if (emailValue.isEmpty()) {
            _emailErrorText.value = "이메일을 입력해주세요"
            return
        }

        if (Validation.isValidateEmail(emailValue).not()) {
            _emailErrorText.value = "이메일 형식이 올바르지 않습니다"
        } else {
            _emailErrorText.value = ""
        }
    }

    fun onPasswordChanged() {
        val passwordValue = password.value

        if (passwordValue.isEmpty()) {
            _passwordErrorText.value = "비밀번호를 입력해주세요"
            return
        }

        if (Validation.isValidatePassword(passwordValue).not()) {
            _passwordErrorText.value = "비밀번호는 6~20글자 이어야 합니다"
        } else {
            _passwordErrorText.value = ""
        }
    }

    fun onPasswordCheckChanged() {
        val passwordCheckValue = passwordCheck.value
        val passwordValue = password.value

        if (passwordCheckValue.isEmpty()) {
            _passwordCheckErrorText.value = ""
            return
        }

        if (passwordCheckValue != passwordValue) {
            _passwordCheckErrorText.value = "비밀번호가 일치하지 않습니다"
        } else {
            _passwordCheckErrorText.value = ""
        }
    }

    fun onNicknameChanged() {
        val nicknameValue = nickname.value
        if (nicknameValue.isEmpty()) {
            _nicknameErrorText.value = "닉네임을 입력해주세요"
            return
        }

        if (Validation.isValidateNickname(nicknameValue).not()) {
            _nicknameErrorText.value = "닉네임은 2~10글자이어야 합니다 (특수문자 불가)"
        } else {
            _nicknameErrorText.value = ""
        }
    }

    fun register(){

    }
}