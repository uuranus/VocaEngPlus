package com.vocaengplus.vocaengplus.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.ui.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val id = MutableLiveData("")
    val password = MutableLiveData("")

    private val _idErrorText = MutableLiveData("")
    val idErrorText:LiveData<String> get() = _idErrorText

    private val _passwordErrorText = MutableStateFlow<String>("")
    val passwordErrorText:StateFlow<String> get() = _passwordErrorText

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage


    fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        println("!!!!!!!")
    }


    fun onIdChanged() {

        println("onIdChanged")
        val idValue = id.value.takeIf { it.isNullOrEmpty().not() } ?: return

        if (Validation.isValidateEmail(idValue).not()) {
            _idErrorText.value = "5~20자의 영문 소문자, 숫자와 특수기호_,- 만 사용 가능합니다"
        } else {
            _idErrorText.value =  ""
        }
    }

    fun onPasswordChanged(){
        val inputPassword = password.value ?: ""
        println("password $inputPassword")
        if(Validation.isValidatePassword(inputPassword).not()){
            _passwordErrorText.value = "비밀번호가 올바르지 않습니다"
        }
    }

}