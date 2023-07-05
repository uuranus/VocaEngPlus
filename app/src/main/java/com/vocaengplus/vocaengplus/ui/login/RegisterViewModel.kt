package com.vocaengplus.vocaengplus.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.data.newData.User
import com.vocaengplus.vocaengplus.model.data.repository.LoginRepository
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val passwordCheck = MutableStateFlow("")
    val nickname = MutableStateFlow("")

    private val _emailErrorState = MutableStateFlow(ErrorState(true, ""))
    val emailErrorState: StateFlow<ErrorState> get() = _emailErrorState

    private val _passwordErrorState = MutableStateFlow(ErrorState(true, ""))
    val passwordErrorState: StateFlow<ErrorState> get() = _passwordErrorState

    private val _passwordCheckErrorState = MutableStateFlow(ErrorState(true, ""))
    val passwordCheckErrorState: StateFlow<ErrorState> get() = _passwordCheckErrorState

    private val _nicknameErrorState = MutableStateFlow(ErrorState(true, ""))
    val nicknameErrorState: StateFlow<ErrorState> get() = _nicknameErrorState

    private val _toastMessage = MutableStateFlow("")
    val toastMessage: StateFlow<String> get() = _toastMessage

    private val _isAllSuccess = MutableStateFlow(false)
    val isAllSuccess: StateFlow<Boolean> get() = _isAllSuccess

    fun onEmailChanged() {
        val emailValue = email.value

        if (emailValue.isEmpty()) {
            _emailErrorState.value = ErrorState(false, "이메일을 입력해주세요")
            return
        }

        if (Validation.isValidateEmail(emailValue).not()) {
            _emailErrorState.value = ErrorState(false, "이메일 형식이 올바르지 않습니다")
        } else {
            _emailErrorState.value = ErrorState(true, "")
        }
    }

    fun onPasswordChanged() {
        val passwordValue = password.value

        if (passwordValue.isEmpty()) {
            _passwordErrorState.value = ErrorState(false, "비밀번호를 입력해주세요")
            return
        }

        if (Validation.isValidatePassword(passwordValue).not()) {
            _passwordErrorState.value = ErrorState(false, "비밀번호는 6~20글자 이어야 합니다")
        } else {
            _passwordErrorState.value = ErrorState(true, "")
        }
    }

    fun onPasswordCheckChanged() {
        val passwordCheckValue = passwordCheck.value
        val passwordValue = password.value

        if (passwordCheckValue.isEmpty()) {
            _passwordCheckErrorState.value = ErrorState(false, "비밀번호를 입력해주세요")
            return
        }

        if (passwordCheckValue != passwordValue) {
            _passwordCheckErrorState.value = ErrorState(false, "비밀번호가 일치하지 않습니다")
        } else {
            _passwordCheckErrorState.value = ErrorState(true, "")
        }
    }

    fun onNicknameChanged() {
        val nicknameValue = nickname.value
        if (nicknameValue.isEmpty()) {
            _nicknameErrorState.value = ErrorState(false, "닉네임을 입력해주세요")
            return
        }

        if (Validation.isValidateNickname(nicknameValue).not()) {
            _nicknameErrorState.value = ErrorState(false, "닉네임은 2~10글자이어야 합니다 (특수문자 불가)")
        } else {
            _nicknameErrorState.value = ErrorState(true, "")
        }
    }

    fun register() {
        if (_nicknameErrorState.value.isSuccess.not() ||
            _passwordErrorState.value.isSuccess.not() ||
            _passwordCheckErrorState.value.isSuccess.not() ||
            _nicknameErrorState.value.isSuccess.not()
        ) return

        viewModelScope.launch {
            val registerResult = repository.requestRegister(email.value, password.value)

            if (registerResult.isSuccess) {
                registerResult.getOrNull()?.let {
                    val newUserData = User(
                        it.name,
                        it.uid,
                        System.currentTimeMillis(),
                        false
                    )
                    val result = repository.requestMakeNewUserData(newUserData)
                    if (result.isSuccess) {
                        _isAllSuccess.value = true
                    } else {
                        _toastMessage.value = registerResult.exceptionOrNull()?.message ?: ""
                    }
                }

            } else {
                _toastMessage.value = registerResult.exceptionOrNull()?.message ?: ""
            }
        }

    }
}