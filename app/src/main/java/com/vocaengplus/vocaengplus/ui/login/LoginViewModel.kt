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
class LoginViewModel @Inject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    val id = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _isIdValid = MutableStateFlow(false)

    private val _isPasswordValid = MutableStateFlow(false)

    private val _isLoginSucceed = MutableStateFlow(false)
    val isLoginSucceed: StateFlow<Boolean> get() = _isLoginSucceed

    private val _idErrorText = MutableStateFlow("")
    val idErrorText: StateFlow<String> get() = _idErrorText

    private val _passwordErrorText = MutableStateFlow("")
    val passwordErrorText: StateFlow<String> get() = _passwordErrorText

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _isUserInValid = MutableStateFlow(false)
    val isUserInValid: StateFlow<Boolean> get() = _isUserInValid

    fun onIdChanged() {

        val idValue = id.value

        if (idValue.isEmpty()) {
            _idErrorText.value = "아이디를 입력해주세요"
            return
        }

        if (Validation.isValidateEmail(idValue).not()) {
            _isIdValid.value = false
            _idErrorText.value = "5~20자의 영문 소문자, 숫자와 특수기호_,- 만 사용 가능합니다"
        } else {
            _isIdValid.value = true
            _idErrorText.value = ""
        }
    }

    fun onPasswordChanged() {
        val passwordValue = password.value

        if (passwordValue.isEmpty()) {
            _passwordErrorText.value = "비밀번호를 입력해주세요"
            return
        }

        if (Validation.isValidatePassword(passwordValue).not()) {
            _isPasswordValid.value = false
            _passwordErrorText.value = "비밀번호는 6자리 이상이어야 합니다."
        } else {
            _isPasswordValid.value = true
            _passwordErrorText.value = ""
        }
    }

    fun login() {
        if (_isIdValid.value.not() || _isPasswordValid.value.not()) return

        val idValue = id.value
        val passwordValue = password.value

        viewModelScope.launch {
            val result = repository.requestLogin(idValue, passwordValue)
            if (result.isSuccess) {
                _isLoginSucceed.value = true
                _snackBarMessage.value = "로그인에 성공했습니다"
            } else {
                _isLoginSucceed.value = false
                _snackBarMessage.value = "이메일 또는 비밀번호가 일치하지 않습니다"
            }
        }
    }


    fun loginAsGuest() {
        viewModelScope.launch {
            val result = repository.requestGuestLogin()
            if (result.isSuccess) {
                val userAuth = result.getOrNull()
                if (userAuth != null) {
                    val userData = User(
                        userAuth.name,
                        userAuth.uid,
                        System.currentTimeMillis(),
                        true
                    )
                    val newDataSucceed = repository.requestMakeNewUserData(userData)

                    _isLoginSucceed.value = true
                    _snackBarMessage.value = "환영합니다"
                } else {
                    _isLoginSucceed.value = false
                    _snackBarMessage.value = "게스트 로그인에 실패했습니다"
                }
            } else {
                _isLoginSucceed.value = false
                _snackBarMessage.value = "게스트 로그인에 실패했습니다"
            }
        }
    }

    fun changePassword(email: String) {
        viewModelScope.launch {
             repository.findPassword(email)
            _snackBarMessage.value = "비밀번호 재설정 이메일을 보냈습니다"
        }
    }

    fun logOut(){
        viewModelScope.launch {
            val result = repository.logOut()
            if(result.isSuccess){
                _snackBarMessage.value = "로그아웃되었습니다"
                _isUserInValid.value = true
            }
            else{
                _snackBarMessage.value = "로그아웃에 실패했습니다"
                _isUserInValid.value = false
            }
        }
    }

    fun quit(){
        viewModelScope.launch {
            val result = repository.quit()
            if(result.isSuccess){
                _snackBarMessage.value = "다음에 또 만나요!"
                _isUserInValid.value = true
            }
            else{
                _snackBarMessage.value = "계정 삭제에 실패했습니다."
                _isUserInValid.value = false
            }
        }
    }

}
