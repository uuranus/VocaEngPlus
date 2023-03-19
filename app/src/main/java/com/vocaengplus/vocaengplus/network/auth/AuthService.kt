package com.vocaengplus.vocaengplus.network.auth

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vocaengplus.vocaengplus.model.data.new.UserAuth

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

    fun getCurrentUserIdToken(successHandler: (String) -> Unit, errorHandler: (String) -> Unit) {
        firebaseAuth.currentUser?.let { user ->
            user.getIdToken(true)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        successHandler(it.result.token ?: "")
                    } else {
                        errorHandler("토큰을 가져오는데 실패했습니다")
                    }
                }
        } ?: errorHandler("로그인이 되어있지 않습니다")
    }

    fun login(email: String, password: String, successHandler: (String) -> Unit) {
        loginWithEmailAndPassword(email, password, successHandler)
    }

    fun loginWithGoogle(
        credential: GoogleAuthCredential,
        successHandler: (String) -> Unit
    ) {
        loginWithCredential(credential, successHandler)
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
        successHandler: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.user
                    user?.let { u ->
                        successHandler(
                            u.uid
//                            UserAuth(
//                                u.uid,
//                                u.email ?: "GUEST",
//                                u.displayName ?: "익명",
//                                u.photoUrl
//                            )
                        )
                    }
                } else {
//                    callback(Result.failure(Exception("로그인에 실패하였습니다")))
                }
            }
    }

    private fun loginWithCredential(
        credential: AuthCredential,
        successHandler: (String) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                user?.let { u ->
                    successHandler(
                        u.uid
//                        UserAuth(
//                            u.uid,
//                            u.email ?: "GUEST",
//                            u.displayName ?: "익명",
//                            u.photoUrl
//                        )
                    )
                }
            } else {
//                callback(Result.failure(Exception("로그인에 실패하였습니다")))
            }
        }
    }

}