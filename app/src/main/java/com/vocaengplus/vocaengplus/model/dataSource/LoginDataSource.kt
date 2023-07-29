package com.vocaengplus.vocaengplus.model.dataSource

import com.vocaengplus.vocaengplus.model.data.UserAuth
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDTO
import com.vocaengplus.vocaengplus.network.dto.WordDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import com.vocaengplus.vocaengplus.network.service.DatabaseService
import retrofit2.Response
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val authService: AuthService,
    private val databaseService: DatabaseService,
) {

    suspend fun login(email: String, password: String): Result<UserAuth> {
        return authService.login(email, password)
    }

    suspend fun loginAsGuest(): Result<UserAuth> {
        return authService.loginAsGuest()
    }

    suspend fun makeNewUserData(idToken: String, userData: UserDTO): Response<UserDTO> {
        return databaseService.putUserData(userData.uid, idToken, userData)
    }

    suspend fun makeDefaultWordList(
        idToken: String,
        uid: String,
        data: WordListDTO,
    ): Response<PostDTO> {
        return databaseService.postWordList(uid, idToken, data)
    }

    suspend fun makeDefaultWords(
        idToken: String,
        uid: String,
        wordListId: String,
        words: List<WordDTO>,
    ): Result<Boolean> {
        val networkResponse = databaseService.putWords(uid, wordListId, idToken, words)

        return if (networkResponse.isSuccessful) {
            networkResponse.body()?.let {
                Result.success(true)
            } ?: Result.failure(Exception())
        } else {
            Result.failure(Exception())
        }
    }

    suspend fun logOut(): Result<Boolean> {
        return authService.logOut()
    }

    suspend fun register(email: String, password: String): Result<UserAuth> {
        return authService.register(email, password)
    }

    suspend fun quit(uid: String, token: String): Result<Boolean> {
        //TODO 다 통과 할 때까지 무한루프도는데 5초 넘으면 fail 리턴해서 사용자가 재시도할 수 있게해줌
//
//        var result = databaseService.deleteUserData(uid, token)
//        if (result.isSuccessful.not()) return Result.failure(Exception())
//        result = databaseService.deleteAllUserWordList(uid, token)
//
//        if (result.isSuccessful.not()) return Result.failure(Exception())
//        //storage 추가하면 프로필 사진도 삭제
//        val lastResult = authService.quit()
//        if (lastResult.isSuccess) return Result.success(true)

        return Result.failure(Exception())
    }

    suspend fun setNewPassword(email: String) {
        return authService.setNewPassword(email)
    }

}