package com.vocaengplus.vocaengplus.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vocaengplus.vocaengplus.model.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _snackBarMessage = MutableStateFlow("")
    val snackBarMessage: StateFlow<String> get() = _snackBarMessage

    private val _isGuest = MutableStateFlow(false)
    val isGuest: StateFlow<Boolean> get() = _isGuest

    private val _imageUrl = MutableStateFlow<Uri?>(null)
    val imageUrl: StateFlow<Uri?> get() = _imageUrl

    val nickname = MutableStateFlow("")

    private val _isSaveSuccess = MutableStateFlow(false)
    val isSaveSuccess: StateFlow<Boolean> get() = _isSaveSuccess

    fun getCurrentProfile() {
        viewModelScope.launch {
            val user = userRepository.getUserInfo()

            if (user == null) {
                _isGuest.value = true
                return@launch
            }

            _imageUrl.value = user.photoUrl
            nickname.value = user.name
        }
    }

    fun setNewProfileImage(uri: Uri) {
        _imageUrl.value = uri
    }

    fun saveNewProfile() {
        if (isValidateNickname().not()) return

        viewModelScope.launch {
            userRepository.saveNewProfile(_imageUrl.value, nickname.value)

            _snackBarMessage.value = "프로필 변경에 성공했습니다"
            _isSaveSuccess.value = true
        }
    }

    private fun isValidateNickname(): Boolean {
        val nick = nickname.value

        if (nick.isEmpty()) {
            _snackBarMessage.value = "닉네임을 입력해주세요"
            return false
        }

        if (nick.length > 10 || nick.length < 2) {
            _snackBarMessage.value = "닉네임은 2~10자 이내여야 합니다"
            return false
        }

        return true
    }
}