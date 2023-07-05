package com.vocaengplus.vocaengplus.model.data

import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import com.vocaengplus.vocaengplus.network.DatabaseService
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.network.dto.PostDTO
import com.vocaengplus.vocaengplus.network.dto.UserDTO
import com.vocaengplus.vocaengplus.network.dto.WordListDTO
import retrofit2.Response
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val authService: AuthService,
    private val databaseService: DatabaseService
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
        data: WordListDTO
    ): Response<PostDTO> {
        return databaseService.postWordList(uid, idToken, data)
    }

    suspend fun makeUserWordList(
        idToken: String,
        uid: String,
        wordListId: String
    ): Response<Boolean> {
        return databaseService.putUserWordList(uid, wordListId, idToken, true)
    }

    suspend fun logOut(): Result<Boolean> {
        return authService.logOut()
    }

    suspend fun register(email: String, password: String): Result<UserAuth> {
        return authService.register(email, password)
    }

    suspend fun quit(uid: String, token: String): Result<Boolean> {
        //TODO 다 통과 할 때까지 무한루프도는데 5초 넘으면 fail 리턴해서 사용자가 재시도할 수 있게해줌

        var result = databaseService.deleteUserData(uid, token)
        if (result.isSuccessful.not()) return Result.failure(Exception())
        result = databaseService.deleteAllUserWordList(uid, token)

        if (result.isSuccessful.not()) return Result.failure(Exception())
        //storage 추가하면 프로필 사진도 삭제
        val lastResult = authService.quit()
        if (lastResult.isSuccess) return Result.success(true)

        return Result.failure(Exception())
    }

    suspend fun setNewPassword(email: String) {
        return authService.setNewPassword(email)
    }

}