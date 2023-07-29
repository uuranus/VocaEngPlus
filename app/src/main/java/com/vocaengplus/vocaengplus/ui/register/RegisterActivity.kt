package com.vocaengplus.vocaengplus.ui.register

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        binding.run {
            lifecycleOwner = this@RegisterActivity
            viewModel = registerViewModel

            registerButton.setOnClickListener {
                registerViewModel.register()
            }
        }

        registerViewModel.run {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    isAllSuccess.collectLatest {
                        if (it) {
                            finish()
                        }
                    }
                }
            }
        }

//        init()
    }

//            registerButton.setOnClickListener {
//
//                val email = emailEditText.text.toString()
//                pw = passwordEditText.text.toString()
//                nickname = nicknameEditText.text.toString()
//                val repw = passwordCheckEditText.text.toString()
//
//                //다시 한 번 더 확인
//                if (validation.checkInput(arrayOf(email, pw, repw, nickname))) {
//                    if (validation.isValidateEmail(email) && validation.isValidatePassword(pw)
//                        && validation.isValidateNickname(nickname)
//                    ) {
//                        if (pw == repw) {
//                            //파이어베이스 인증 시작
//                            if (firebaseauth.currentUser != null && initialization.getFBuser().isAnonymous) {
//                                val credential = EmailAuthProvider.getCredential(email, pw)
//                                firebaseauth.currentUser!!.linkWithCredential(credential)
//                                    .addOnCompleteListener(this@RegisterActivity) { task ->
//                                        if (task.isSuccessful) {
//                                            Toast.makeText(
//                                                baseContext, "계정 연결 성공",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                            initialization.setCurrentUser()
//                                            initialization.initCommunity()
//                                            databaseref.child("UserData")
//                                                .child(firebaseauth.currentUser!!.uid)
//                                                .child("emailId").setValue(email)
//                                            databaseref.child("UserData")
//                                                .child(firebaseauth.currentUser!!.uid)
//                                                .child("nickname").setValue(nickname)
//                                            databaseref.child("UserData")
//                                                .child(firebaseauth.currentUser!!.uid)
//                                                .child("downloadData").get().addOnSuccessListener {
//                                                    for (c in it.children) {
//                                                        if (c.child("categorywritertoken").value.toString() == firebaseauth.currentUser!!.uid) {
//                                                            databaseref.child("UserData")
//                                                                .child(firebaseauth.currentUser!!.uid)
//                                                                .child("downloadData")
//                                                                .child(c.key.toString())
//                                                                .child("categorywriter")
//                                                                .setValue(nickname)
//                                                        }
//                                                    }
//                                                }
//                                            //데이터들 다 업데이트
//                                            val profileUpdates = userProfileChangeRequest {
//                                                displayName = nickname
//                                            }
//
//                                            firebaseauth.currentUser!!.updateProfile(profileUpdates)
//                                                .addOnCompleteListener {
//                                                    if (task.isSuccessful) {
//
//                                                        firebaseauth.currentUser!!.sendEmailVerification()
//                                                            .addOnCompleteListener {
//                                                                if (it.isSuccessful) {
//                                                                    Toast.makeText(
//                                                                        this@RegisterActivity,
//                                                                        "이메일 인증을 해주세요",
//                                                                        Toast.LENGTH_SHORT
//                                                                    ).show()
//
//                                                                    val intent = Intent(
//                                                                        this@RegisterActivity,
//                                                                        LoginActivity::class.java
//                                                                    )
//                                                                    intent.flags =
//                                                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                                    intent.putExtra(
//                                                                        "nickname",
//                                                                        nickname
//                                                                    )
//                                                                    startActivity(intent)
//                                                                    finish()
//                                                                }
//                                                            }
//                                                    }
//                                                }
//                                        } else {
//                                            Toast.makeText(
//                                                baseContext, "계정 연결 실패",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    }
//                            } else {
//                                firebaseauth.createUserWithEmailAndPassword(email, pw)
//                                    .addOnCompleteListener { task ->
//                                        if (task.isSuccessful) {
//                                            val img = random.nextInt(3)
//
//                                            val profile_url = initialization.getStorageref()
//                                                .child("profile_images/" + default[img]).path
//
//                                            val profileUpdates = userProfileChangeRequest {
//                                                displayName = nickname
//                                                photoUri = Uri.parse(profile_url)
//                                            }
//
//                                            firebaseauth.currentUser!!.updateProfile(profileUpdates)
//                                                .addOnCompleteListener {
//                                                    if (task.isSuccessful) {
//
//                                                        firebaseauth.currentUser!!.sendEmailVerification()
//                                                            .addOnCompleteListener {
//                                                                if (it.isSuccessful) {
//                                                                    Toast.makeText(
//                                                                        this@RegisterActivity,
//                                                                        "이메일 인증을 해주세요",
//                                                                        Toast.LENGTH_SHORT
//                                                                    ).show()
//
//                                                                    val intent = Intent(
//                                                                        this@RegisterActivity,
//                                                                        LoginActivity::class.java
//                                                                    )
//                                                                    intent.flags =
//                                                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                                                    intent.putExtra(
//                                                                        "nickname",
//                                                                        nickname
//                                                                    )
//                                                                    startActivity(intent)
//                                                                    finish()
//                                                                }
//                                                            }
//                                                    }
//                                                }
//
//                                        } else {
//                                            //데이터베이스에서 중복 확인하여 중복되면 중복 toast 띄우기 ( 회원가입 실패 이유 )
//                                            if (firebaseauth.currentUser!!.isEmailVerified) {
//                                                Toast.makeText(
//                                                    this@RegisterActivity,
//                                                    "이미 존재하는 이메일입니다",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            } else {
//                                                Toast.makeText(
//                                                    this@RegisterActivity,
//                                                    "이메일 인증을 해주세요",
//                                                    Toast.LENGTH_SHORT
//                                                ).show()
//                                            }
//                                        }
//                                    }
//                            }
//
//                        } else {
//                            Toast.makeText(
//                                this@RegisterActivity,
//                                "비밀번호가 같지 않습니다",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                            return@setOnClickListener
//                        }
//                    } else {
//                        Toast.makeText(this@RegisterActivity, "회원정보가 올바르지 않습니다", Toast.LENGTH_SHORT)
//                            .show()
//                        return@setOnClickListener
//
//                    }
//                } else {
//                    Toast.makeText(this@RegisterActivity, "회원정보를 입력하세요!", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//            }
//
//        }
//    }


}