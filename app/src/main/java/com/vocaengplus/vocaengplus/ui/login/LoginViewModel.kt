package com.vocaengplus.vocaengplus.ui.login

import android.widget.EditText
import androidx.lifecycle.ViewModel
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.ui.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    val id = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _idErrorText = MutableStateFlow("")
    val idErrorText:StateFlow<String> get() = _idErrorText

    private val _passwordErrorText = MutableStateFlow<String>("")
    val passwordErrorText:StateFlow<String> get() = _idErrorText

    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    fun onIdChanged() {
        val inputId = id.value
        println("inputId $inputId")
        if (Validation.isValidateEmail(inputId).not()) {
            _idErrorText.value = "아이디가 올바르지 않습니다"
        }
    }

    fun onPasswordChanged(){
        val inputPassword = password.value
        println("password $inputPassword")
        if(Validation.isValidatePassword(inputPassword).not()){
            _passwordErrorText.value = "비밀번호가 올바르지 않습니다"
        }
    }

}