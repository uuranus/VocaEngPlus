package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityRegisterBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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

//        init()
    }

//    private fun init() {
//
//        binding.apply {
//            emailEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (!validation.checkInput(arrayOf(emailEditText.text.toString()))) {
//                        emailLayout.error = "이메일을 입력해주세요"
//                    } else if (!validation.isValidateEmail(emailEditText.text.toString())) {
//                        emailLayout.error = "이메일 형식이 올바르지 않습니다"
//                    } else {
//                        emailLayout.error = null
//                    }
//                }
//
//            })
//
//            passwordEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (!validation.checkInput(arrayOf(passwordEditText.text.toString()))) {
//                        passwordLayout.error = "비밀번호를 입력해주세요"
//                    } else if (!validation.isValidatePassword(passwordEditText.text.toString())) {
//                        passwordLayout.error =
//                            "비밀번호는 영문자,숫자,특수문자(!@#\$%^*/ 중에서) 각각 1개 이상 포함 8~20자이어야 합니다"
//                    } else {
//                        passwordLayout.error = null
//                    }
//                }
//
//            })
//
//            passwordCheckEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (!validation.checkInput(arrayOf(passwordCheckEditText.text.toString()))) {
//                        passwordCheckLayout.error = "비밀번호를 다시 입력해주세요"
//                    } else if (passwordCheckEditText.text.toString() != passwordEditText.text.toString()) {
//                        passwordCheckLayout.error = "비밀번호가 일치하지 않습니다"
//                    } else {
//                        passwordCheckLayout.error = null
//                    }
//                }
//
//            })
//
//            nicknameEditText.addTextChangedListener(object : TextWatcher {
//                override fun beforeTextChanged(
//                    s: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//                override fun afterTextChanged(s: Editable?) {
//                    if (!validation.checkInput(arrayOf(nicknameEditText.text.toString()))) {
//                        nicknameLayout.error = "닉네임을 입력해주세요"
//                    } else if (!validation.isValidateNickname(nicknameEditText.text.toString())) {
//                        nicknameLayout.error = "닉네임은 2~10글자이어야 합니다 (특수문자 불가)"
//                    } else if (nicknameEditText.text.toString() == "Default") {
//                        nicknameLayout.error = "Default 닉네임은 사용불가합니다"
//                    } else {
//                        nicknameLayout.error = null
//                    }
//                }
//
//            })
//
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