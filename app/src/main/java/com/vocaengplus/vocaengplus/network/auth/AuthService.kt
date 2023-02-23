package com.vocaengplus.vocaengplus.network.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vocaengplus.vocaengplus.model.data.new.Fail
import com.vocaengplus.vocaengplus.model.data.new.NetworkState
import com.vocaengplus.vocaengplus.model.data.new.Success
import com.vocaengplus.vocaengplus.model.data.new.UserAuth

object AuthService {
    private val firebaseAuth = Firebase.auth

    fun getCurrentUID(): String = firebaseAuth.currentUser?.uid ?: ""

    fun getCurrentUserInfo(): UserAuth? {
        firebaseAuth.currentUser ?: return null

        return firebaseAuth.currentUser?.let {
            UserAuth(
                it.uid,
                it.email ?: "GUEST",
                it.displayName ?: "익명",
                it.photoUrl
            )
        }
    }

    fun getCurrentUserIdToken(callback: (Result<String>) -> Unit) {
        firebaseAuth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback(Result.success(it.result.token ?: ""))
                    } else {
                        callback(Result.failure(Exception()))
                    }
                }
        } ?: callback(Result.failure(Exception("로그인이 되어있지 않습니다")))
    }

    fun login(email: String, password: String, callback: (NetworkState<UserAuth>) -> Unit) =
        loginWithEmailAndPassword(email, password, callback)

    fun loginWithGoogle(
        credential: GoogleAuthCredential,
        callback: (NetworkState<UserAuth>) -> Unit
    ) = loginWithCredential(credential, callback)


    fun logOut() = firebaseAuth.signOut()

    fun quit() = firebaseAuth.currentUser?.delete()

    private fun loginWithEmailAndPassword(
        email: String,
        password: String,
        callback: (NetworkState<UserAuth>) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.user
                    user?.let { u ->
                        callback(
                            Success(
                                UserAuth(
                                    u.uid,
                                    u.email ?: "GUEST",
                                    u.displayName ?: "익명",
                                    u.photoUrl
                                )
                            )
                        )
                    }
                    Result
                } else {
                    callback(Fail("로그인에 실패하였습니다"))
                }
            }
    }

    private fun loginWithCredential(
        credential: AuthCredential,
        callback: (NetworkState<UserAuth>) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                user?.let { u ->
                    callback(
                        Success(
                            UserAuth(
                                u.uid,
                                u.email ?: "GUEST",
                                u.displayName ?: "익명",
                                u.photoUrl
                            )
                        )
                    )
                }
                Result
            } else {
                callback(Fail("로그인에 실패하였습니다"))
            }
        }
    }

}