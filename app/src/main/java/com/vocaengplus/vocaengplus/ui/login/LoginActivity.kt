package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.UserAccount
import com.vocaengplus.vocaengplus.ui.util.Validation
import com.vocaengplus.vocaengplus.databinding.ActivityLoginBinding
import com.vocaengplus.vocaengplus.databinding.FindpwBinding
import com.vocaengplus.vocaengplus.databinding.GuestBinding
import com.vocaengplus.vocaengplus.di.Initialization
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

class LoginActivity : AppCompatActivity() {

    lateinit var firebaseauth: FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref: DatabaseReference    // 실시간 데이터베이스
    lateinit var useraccount: UserAccount
    lateinit var binding: ActivityLoginBinding
    val initialization = Initialization
    lateinit var validation: Validation
    var nickname: String = ""
    val default = arrayOf("default1.png", "default2.png", "default3.png")
    val random = Random()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)

        binding.run{
            lifecycleOwner = this@LoginActivity
            viewModel = loginViewModel
        }

        loginViewModel.run {
            id.observe(this@LoginActivity) {
                println("iddddddd $it")
            }
        }
        init()
    }

    override fun onResume() {
        super.onResume()
        if (intent.hasExtra("nickname")) {
            nickname = intent.getStringExtra("nickname").toString()
        }
    }

    private fun init() {
        initialization.setDatabase()
        firebaseauth = initialization.getFBauth()
        databaseref = initialization.getDBref()
        validation = Validation

        binding.apply {
            registerTextView.setOnClickListener {
                //회원가입 화면 전환
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            findPasswordTextView.setOnClickListener {
                val dlgBinding = FindpwBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setView(dlgBinding.root)
                    .setPositiveButton("확인") { _, _ ->
                        val email = dlgBinding.emailforpw.text.toString()
                        if (!validation.checkInput(arrayOf(email))) {
                            Toast.makeText(this@LoginActivity, "이메일을 입력해주세요", Toast.LENGTH_SHORT)
                                .show()
                            return@setPositiveButton
                        }
                        if (!validation.isValidateEmail(email)) {
                            Toast.makeText(
                                this@LoginActivity,
                                "이메일이 올바르지 않습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@setPositiveButton
                        }
                        Toast.makeText(
                            this@LoginActivity,
                            "비밀번호 재설정 이메일을 전송하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        initialization.getFBauth().sendPasswordResetEmail(email)
                            .addOnSuccessListener { it2 ->
                                Toast.makeText(
                                    this@LoginActivity,
                                    "비밀번호 재설정 이메일을 보냈습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            .addOnCanceledListener {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "비밀번호 재설정 이메일을 보내는데 실패했습니다..",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        return@setPositiveButton


                    }
                    .setNegativeButton("취소") { _, _ ->
                        {

                        }
                    }
                val dlg = builder.create()
                dlg.show()
            }

            loginButton.setOnClickListener {
//                if(emailEditText.text.isBlank()||passwordEditText.text.isBlank()){
//                    Toast.makeText(this@LoginActivity,"아이디, 비밀번호를 입력하세요.",Toast.LENGTH_SHORT).show()
//                }
//                else{
                //로그인 요청
//                    firebaseauth.signInWithEmailAndPassword(emailEditText.text.toString(),passwordEditText.text.toString()).addOnCompleteListener { task ->
//                        if (task.isSuccessful) {
//                            //유효한 회원인지 확인
//                            if(firebaseauth.currentUser?.isEmailVerified == true){
//                                databaseref.child("UserData").get().addOnSuccessListener {
//                                    if (it.hasChild(firebaseauth.currentUser!!.uid)) {
//                                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                        intent.flags=
//                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        intent.putExtra("isLogined", true)
//                                        startActivity(intent)
//                                        finish()
//                                    } else { //재로그인하는 경우
//                                        initialization.setCurrentUser()
//                                        initialization.initUserData(nickname)
//                                        initialization.initCommunity()
//                                        initialization.initUserLog()
//
//                                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
//                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                        intent.flags=
//                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
//                                        intent.putExtra("isLogined", true)
//                                        startActivity(intent)
//                                        finish()
//                                    }
//                                }
//                            }
//                            else{
//                                Toast.makeText(this@LoginActivity, "이메일 인증을 해주세요.", Toast.LENGTH_SHORT).show()
//                            }
//                        } else {
//                            //로그인 실패
//                            Toast.makeText(this@LoginActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    }

            }


            guestButton.setOnClickListener {
                val dlgBinding = GuestBinding.inflate(layoutInflater)
                val builder = AlertDialog.Builder(this@LoginActivity)
                builder.setView(dlgBinding.root)
                    .setPositiveButton("확인") { _, _ ->
                        //파이어베이스 인증 시작

                        firebaseauth.signInAnonymously().addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val img = random.nextInt(3)

                                val profile_url = initialization.getStorageref()
                                    .child("profile_images/" + default[img]).path

                                val profileUpdates = userProfileChangeRequest {
                                    displayName = "게스트"
                                    photoUri = Uri.parse(profile_url)
                                }

                                firebaseauth.currentUser!!.updateProfile(profileUpdates)
                                    .addOnCompleteListener {
                                        if (task.isSuccessful) {

                                            initialization.setCurrentUser()
                                            initialization.initUserData("게스트")
                                            initialization.initUserLog()

                                            Toast.makeText(
                                                this@LoginActivity,
                                                "로그인 성공",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val intent =
                                                Intent(this@LoginActivity, MainActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP
                                            intent.putExtra("isLogined", true)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }

                            } else {
                                //데이터베이스에서 중복 확인하여 중복되면 중복 toast 띄우기 ( 회원가입 실패 이유 )
                                Toast.makeText(this@LoginActivity, "게스트 가입 실패", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                    }
                    .setNegativeButton("취소") { _, _ ->
                        {

                        }
                    }
                val dlg = builder.create()
                dlg.show()
            }
        }
    }
}