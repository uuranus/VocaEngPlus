package com.vocaengplus.vocaengplus.network.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

object AuthService {
    private val firebaseAuth = Firebase.auth
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val random = Random(System.currentTimeMillis())
    private val default = arrayOf("default1.png", "default2.png", "default3.png")

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

    suspend fun getCurrentUserIdToken(): String {
        firebaseAuth.currentUser?.let { user ->
            val resultToken = user.getIdToken(true).await()
            if (resultToken.token != null) {
                return resultToken.token ?: ""
            } else {
                return ""
            }
        } ?: return ""
    }

    suspend fun login(email: String, password: String): Result<UserAuth> {
        var result = Result.success(UserAuth("", "", "", null))
        runBlocking {
            result = loginWithEmailAndPassword(email, password)
            println("serviceeeee $result")
        }

        return result
    }

    fun loginWithGoogle(
        credential: GoogleAuthCredential,
        callback: (Result<UserAuth>) -> Unit
    ) {
        loginWithCredential(credential, callback)
    }

    fun logOut() {
        firebaseAuth.signOut()
    }

    fun quit() {
        firebaseAuth.currentUser?.delete()
    }

    private suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): Result<UserAuth> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            println("auth $authResult")

            authResult.user?.let {
                Result.success(UserAuth(it.uid, it.email ?: "", it.displayName ?: "", it.photoUrl))
            } ?: Result.failure(Exception("로그인에 실패했습니다"))

        } catch (e: FirebaseException) {
            Result.failure(Exception("로그인에 실패하였습니다"))
        }
    }

    private fun loginWithCredential(
        credential: AuthCredential,
        callback: (Result<UserAuth>) -> Unit
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.user
                user?.let { u ->
                    Result.success(
                        UserAuth(
                            u.uid,
                            u.email ?: "GUEST",
                            u.displayName ?: "익명",
                            u.photoUrl
                        )
                    )
                }
            } else {
                callback(Result.failure(Exception("로그인에 실패하였습니다")))
            }
        }
    }

    suspend fun loginAsGuest(): Result<UserAuth> {
        return try {
            val authResult = firebaseAuth.signInAnonymously().await()
            println("authResult ${authResult.user}")

            authResult.user?.let {
                Result.success(
                    UserAuth(it.uid, it.email ?: "", it.displayName ?: "", it.photoUrl)
                )
            } ?: Result.failure(Exception("게스트 로그인에 실패했습니다"))
        } catch (e: FirebaseException) {
            Result.failure(Exception("로그인에 실패하였습니다"))
        }
    }


    suspend fun register(email: String, password: String): Result<UserAuth> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            authResult.user?.let {
                Result.success(UserAuth(it.uid, it.email ?: "", it.displayName ?: "", it.photoUrl))
            } ?: Result.failure(Exception("로그인에 실패했습니다"))

        } catch (e: FirebaseException) {
            Result.failure(Exception("로그인에 실패하였습니다"))
        }
    }
}

