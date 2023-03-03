package com.vocaengplus.vocaengplus.network.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vocaengplus.vocaengplus.model.data.new.Fail
import com.vocaengplus.vocaengplus.model.data.new.NetworkState
import com.vocaengplus.vocaengplus.model.data.new.Success
import com.vocaengplus.vocaengplus.model.data.new.UserAuth
import com.vocaengplus.vocaengplus.network.NetworkCallback

object AuthService {
    private val firebaseAuth = Firebase.auth

    fun getCurrentUID(): String {
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    fun getCurrentUserInfo(): UserAuth? {
        return firebaseAuth.currentUser?.let {
            UserAuth(
                it.uid,
                it.email ?: "GUEST",
                it.displayName ?: "익명",
                it.photoUrl
            )
        }
    }

    fun getCurrentUserIdToken(callback: NetworkCallback) {
        firebaseAuth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        callback.onDataLoaded(Result.success(it.result.token ?: ""))
                    } else {
                        callback.onDataFailed(Result.failure<UserAuth>(Exception()))
                    }
                }
        } ?: callback.onDataFailed(Result.failure<UserAuth>(Exception("로그인이 되어있지 않습니다")))
    }

    fun login(email: String, password: String, callback: NetworkCallback) {
        loginWithEmailAndPassword(email, password, callback)
    }

    fun loginWithGoogle(
        credential: GoogleAuthCredential,
        callback: NetworkCallback
    ) {
        loginWithCredential(credential, callback)
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun quit() {
        firebaseAuth.currentUser?.delete()
    }

    private fun loginWithEmailAndPassword(
        email: String,
        password: String,
        callback: NetworkCallback
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.user
                    user?.let { u ->
                        callback.onDataLoaded(
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
                    callback.onDataFailed(Fail<UserAuth>("로그인에 실패하였습니다"))
                }
            }
    }

    private fun loginWithCredential(
        credential: AuthCredential,
        callback: NetworkCallback
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                user?.let { u ->
                    callback.onDataLoaded(
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
                callback.onDataFailed(Fail<UserAuth>("로그인에 실패하였습니다"))
            }
        }
    }

}