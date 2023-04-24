package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.*
import com.vocaengplus.vocaengplus.adapter.bindingAdapter.setImageUrl
import com.vocaengplus.vocaengplus.databinding.ActivityMainBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.ui.community.CommunityActivity
import com.vocaengplus.vocaengplus.ui.myWord.MyWordActivity
import com.vocaengplus.vocaengplus.ui.review.ReviewActivity
import com.vocaengplus.vocaengplus.ui.search.SearchActivity
import com.vocaengplus.vocaengplus.ui.setting.SettingActivity
import com.vocaengplus.vocaengplus.ui.statistics.StatisticsActivity
import com.vocaengplus.vocaengplus.ui.test.TestActivity
import com.vocaengplus.vocaengplus.ui.wordList.WordActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()

    //drawer header 에 접근하기 위한 값
    private val navHeader by lazy {
        binding.navView.getHeaderView(0)
    }

    private val headerImageView: ImageView by lazy {
        navHeader.findViewById(R.id.profileImageView)
    }

    private val headerNicknameTextView: TextView by lazy {
        navHeader.findViewById(R.id.nicknameTextView)
    }

    private val headerEmailIdTextView: TextView by lazy {
        navHeader.findViewById(R.id.emailTextView)
    }

    private val logOutAlertDialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setMessage("로그아웃 하시겠습니까?\n게스트 계정은 로그아웃 시 데이터가 사라집니다")
            .setPositiveButton("네") { _, _ ->
                viewModel.logOut()
            }
            .setNegativeButton("아니오") { _, _ ->
            }
            .create()
    }

    private val quitAlertDialog: AlertDialog by lazy {
        AlertDialog.Builder(this)
            .setMessage("해당 계정을 삭제하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                viewModel.quit()
            }
            .setNegativeButton("아니오") { _, _ ->
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {

            val idToken = AuthService.getCurrentUserIdToken()
            println("token $idToken")

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isUserInValid.collectLatest {
                    if (it) {
                        val intent = Intent(this@MainActivity, RoutingActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.let {
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }

        initDrawer()

        binding.appBarMain.contentMain.run {
            wordImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, WordActivity::class.java)
                startActivity(intent)
            }

            testImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, TestActivity::class.java)
                startActivity(intent)
            }

            reviewImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, ReviewActivity::class.java)
                startActivity(intent)
            }

            myWordImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, MyWordActivity::class.java)
                startActivity(intent)
            }

            searchImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }

            statisticsImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, StatisticsActivity::class.java)
                startActivity(intent)
            }

            communityImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, CommunityActivity::class.java)
                startActivity(intent)
            }

            settingImageView.setOnClickListener {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //계정 정보가 변경될 수 있으니 resume 할 때마다 확인
    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val userInfo = AuthService.getCurrentUserInfo()

            if (userInfo == null) { //null 이면 로그아웃이나 계정삭제를 한 것
                val intent = Intent(this@MainActivity, RoutingActivity::class.java)
                startActivity(intent)
                finish()
            }

            userInfo?.let { user ->
                println("user $user")
                user.photoUrl?.let {
                    headerImageView.setImageUrl(it)
                }

                headerNicknameTextView.text = user.name
                headerEmailIdTextView.text = user.emailId
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initDrawer() {
        binding.navView.setNavigationItemSelectedListener {
            binding.drawerLayout.closeDrawers()

            when (it.itemId) {
                R.id.nav_profile -> {
                    val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_pwchange -> {
                    val email =
                        binding.navView.getHeaderView(0)
                            .findViewById<TextView>(R.id.emailTextView)
                    viewModel.changePassword(email.text.toString())
                }
                R.id.nav_quit -> {
                    quitAlertDialog.show()
                }
                R.id.nav_logout -> {
                    logOutAlertDialog.show()
                }
                R.id.nav_contact -> {
                    sendEmailToDev()
                }
                else -> {}
            }
            return@setNavigationItemSelectedListener false
        }
    }

    private fun sendEmailToDev() {
        val email = Intent(Intent.ACTION_SEND)
        val address = arrayOf("vocaengplus@gmail.com")
        email.putExtra(Intent.EXTRA_SUBJECT, "VocaEng+ 문의 및 피드백") //제목
        email.putExtra(Intent.EXTRA_EMAIL, address) //받는사람
        email.putExtra(
            Intent.EXTRA_TEXT, String.format(
                "문의 시, 아래 내용을 함께 보내주시면 큰 도움이 됩니다.\n**********\n " +
                        " 사용자 ID: %s\n 앱 버전 : %s\n 기기 : %s\n 안드로이드 OS 버전 : %d(%s)\n ",
                AuthService.getCurrentUID(),
                BuildConfig.VERSION_NAME,
                Build.DEVICE,
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE
            )
        )
        email.type = "message/*"
        startActivity(email)
    }
}