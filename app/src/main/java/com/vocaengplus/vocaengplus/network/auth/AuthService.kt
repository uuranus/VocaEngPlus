package com.vocaengplus.vocaengplus.network.auth

import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.vocaengplus.vocaengplus.model.data.newData.UserAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

object AuthService {
    private val firebaseAuth = Firebase.auth
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val random = Random(System.currentTimeMillis())
    private val default = arrayOf("default1.png", "default2.png", "default3.png")

    fun getCurrentUID(): String {
        println("currentUser ${firebaseAuth.currentUser?.uid}")
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    fun getCurrentUserInfo(): UserAuth? {
        return firebaseAuth.currentUser?.let {
            val email = if (it.email.isNullOrEmpty()) "GUEST" else it.email ?: "GUEST"
            val nickname = if (it.displayName.isNullOrEmpty()) "보카잉" else it.displayName ?: "보카잉"
            UserAuth(
                it.uid,
                email,
                nickname,
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

    //login
    suspend fun login(email: String, password: String): Result<UserAuth> {
        var result = Result.success(UserAuth("", "", "", null))
        runBlocking {
            result = loginWithEmailAndPassword(email, password)
        }

        return result
    }

    fun loginWithGoogle(
        credential: GoogleAuthCredential,
        callback: (Result<UserAuth>) -> Unit
    ) {
        loginWithCredential(credential, callback)
    }

    suspend fun logOut(): Result<Boolean> {
        return try {
            firebaseAuth.signOut() //여기서 await 못하면 예외처리도 안 될텐데
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(Exception())
        }
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

    //register
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

    suspend fun setNewPassword(email: String) {
        firebaseAuth
            .sendPasswordResetEmail(email).await()
    }

    suspend fun quit(): Result<Boolean> {
        return try {
            firebaseAuth.currentUser?.delete()?.await()
            Result.success(true)
        } catch (e: java.lang.Exception) {
            //TODO 로그인한 지 너무 오래됐거나 유효하지 않은 사용자 로그인하는 것 오류 처리해줘야함
            Result.failure(Exception())
        }
    }
}

