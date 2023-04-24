package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityLoginBinding
import com.vocaengplus.vocaengplus.databinding.FindpwBinding
import com.vocaengplus.vocaengplus.databinding.GuestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    var nickname: String = ""
    private val loginViewModel: LoginViewModel by viewModels()

    private val findPasswordAlertDialog: AlertDialog by lazy {
        val dlgBinding = FindpwBinding.inflate(layoutInflater)
        AlertDialog.Builder(this@LoginActivity)
            .setView(dlgBinding.root)
            .setPositiveButton("확인") { _, _ ->
                val email = dlgBinding.emailforpw.text.toString()
                //TODO 이메일 검증
                loginViewModel.changePassword(email)
            }
            .setNegativeButton("취소") { _, _ ->
            }.create()
    }

    private val guestLoginAlertDialog: AlertDialog by lazy {
        val dlgBinding = GuestBinding.inflate(layoutInflater)
        AlertDialog.Builder(this@LoginActivity)
            .setView(dlgBinding.root)
            .setPositiveButton("확인") { _, _ ->
                loginViewModel.loginAsGuest()
            }
            .setNegativeButton("취소") { _, _ ->
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        binding.run {
            lifecycleOwner = this@LoginActivity
            viewModel = loginViewModel
        }

        init()
    }

    private fun init() {

        binding.apply {
            registerTextView.setOnClickListener {
                //회원가입 화면 전환
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(intent)
            }

            findPasswordTextView.setOnClickListener {
                findPasswordAlertDialog.show()
            }

            loginButton.setOnClickListener {
                loginViewModel.login()
            }

            guestButton.setOnClickListener {
                guestLoginAlertDialog.show()
            }
        }

        loginViewModel.run {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    snackBarMessage.collectLatest {
                        if (it.isNotEmpty()) {
                            Toast.makeText(this@LoginActivity, it, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    isLoginSucceed.collectLatest {
                        if (it) {
                            val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()

                        }
                    }
                }
            }
        }
    }
}