package com.vocaengplus.vocaengplus.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.vocaengplus.vocaengplus.*
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.network.auth.AuthService
import com.vocaengplus.vocaengplus.ui.community.CommunityActivity
import com.vocaengplus.vocaengplus.ui.myWord.MyWordActivity
import com.vocaengplus.vocaengplus.ui.profile.ProfileActivity
import com.vocaengplus.vocaengplus.ui.review.ReviewActivity
import com.vocaengplus.vocaengplus.ui.search.SearchActivity
import com.vocaengplus.vocaengplus.ui.setting.SettingActivity
import com.vocaengplus.vocaengplus.ui.statistics.StatisticsActivity
import com.vocaengplus.vocaengplus.ui.test.TestActivity
import com.vocaengplus.vocaengplus.ui.wordList.WordActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    val initialization = Initialization

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if(FirebaseAuth.getInstance().currentUser==null){
//            val intent=Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//        else{
//            init()
//            initDrawer()
//        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initDrawer() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val actionbar = supportActionBar!!
        actionbar.setDisplayShowTitleEnabled(false)
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.navView)
        navView.setNavigationItemSelectedListener {
            drawerLayout.closeDrawers()
            val id = it.itemId

            if (id == R.id.nav_profile) {
                if (!initialization.getFBuser().isAnonymous) {
                    Toast.makeText(this, "계정", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@MainActivity, ProfileActivity::class.java)
                    startActivityForResult(intent, 700)
                } else { //계정 연결 화면으로 이동
                    val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }

            } else if (id == R.id.nav_pwchange) {
                if (!initialization.getFBuser().isAnonymous) {
                    initialization.getFBauth()
                        .sendPasswordResetEmail(initialization.getFBuser().email.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                Toast.makeText(this, "비밀번호 재설정 이메일을 보냈습니다.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }

            } else if (id == R.id.nav_quit) {
                AlertDelete()
            } else if (id == R.id.nav_logout) {
                AlertLogout()
            } else if (id == R.id.nav_contact) {
                SendEmailToDev()
            }

            return@setNavigationItemSelectedListener false
        }

        val nav_header = navView.getHeaderView(0)
        val profile_img = nav_header.findViewById<ImageView>(R.id.profile_img)
        val nickname = nav_header.findViewById<TextView>(R.id.nav_nickname)
        val emailID = nav_header.findViewById<TextView>(R.id.nav_emailID)

        val firebaseUser = initialization.getFBuser()

        if (firebaseUser != null) {
            initialization.firebaseStorage.child(firebaseUser.photoUrl.toString()).downloadUrl.addOnSuccessListener {

                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(profile_img)
            }

            nickname.text = firebaseUser.displayName.toString()
            if (initialization.getFBuser().isAnonymous) {
                emailID.text = "GUEST"
            } else {
                emailID.text = firebaseUser.email.toString()
            }
        }

    }

    private fun init() {
        initialization.setDatabase()
        initialization.setCurrentUser()


        val wordbtn = findViewById<ImageView>(R.id.wordImageView)
        val testbtn = findViewById<ImageView>(R.id.testImageView)
        val reviewbtn = findViewById<ImageView>(R.id.reviewImageView)
        val mywordbtn = findViewById<ImageView>(R.id.myWordImageView)
        val naverbtn = findViewById<ImageView>(R.id.searchImageView)
        val statisticsbtn = findViewById<ImageView>(R.id.statisticsImageView)
        val communitybtn = findViewById<ImageView>(R.id.communityImageView)
        val settingbtn = findViewById<ImageView>(R.id.settingImageView)

        wordbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, WordActivity::class.java)
            startActivity(intent)
        }
        testbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, TestActivity::class.java)
            startActivity(intent)
        }
        reviewbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, ReviewActivity::class.java)
            startActivity(intent)
        }
        mywordbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, MyWordActivity::class.java)
            startActivity(intent)
        }
        naverbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        statisticsbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, StatisticsActivity::class.java)
            startActivity(intent)
        }
        communitybtn.setOnClickListener {
            val intent = Intent(this@MainActivity, CommunityActivity::class.java)
            startActivity(intent)
        }
        settingbtn.setOnClickListener {
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivityForResult(intent, 600)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 600) {
            if (resultCode == RESULT_OK) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else if (requestCode == 700) {
            if (resultCode == RESULT_OK) {
                initDrawer()
            }
        }
    }

    //계정 삭제 알림창
    private fun AlertDelete() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("해당 계정을 삭제하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                //데이터베이스에서 삭제하기
                initialization.databaseref.child("UserData").child(initialization.uid).removeValue()
                initialization.databaseref.child("UserDownload").child(initialization.uid)
                    .removeValue()
                initialization.databaseref.child("Community").child(initialization.uid)
                    .removeValue()
                initialization.databaseref.child("UserLog").child(initialization.uid).removeValue()
                initialization.firebaseUser.delete().addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "계정 삭제 완료", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else { //로그인한 지 너무 오래됐다면
                        val snackbar = Snackbar.make(
                            drawerLayout,
                            "로그인한 지 오래되었습니다.\n다시 로그인해주세요",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        snackbar.setAction("확인", View.OnClickListener {
                            snackbar.dismiss()
                            val intent = Intent(this@MainActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        })
                        snackbar.show()
                    }

                }
            }
            .setNegativeButton("아니오") { _, _ ->
            }
        val dlg = builder.create()
        dlg.show()
    }

    //로그아웃 알림창
    private fun AlertLogout() {
        if (initialization.getFBuser().isAnonymous) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("로그아웃 하시겠습니까?\n게스트 계정은 로그아웃 시 데이터가 사라집니다")
                .setPositiveButton("네") { _, _ ->
                    initialization.firebaseUser.delete().addOnSuccessListener {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
                .setNegativeButton("아니오") { _, _ ->
                }
            val dlg = builder.create()
            dlg.show()
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    initialization.firebaseAuth.signOut()
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("아니오") { _, _ ->
                }
            val dlg = builder.create()
            dlg.show()
        }

    }

    private fun SendEmailToDev() {
        val email = Intent(Intent.ACTION_SEND)
        val address = arrayOf("vocaengplus@gmail.com")
        email.putExtra(Intent.EXTRA_SUBJECT, "VocaEng+ 문의 및 피드백") //제목
        email.putExtra(Intent.EXTRA_EMAIL, address) //받는사람
        email.putExtra(
            Intent.EXTRA_TEXT, String.format(
                "문의 시, 아래 내용을 함께 보내주시면 큰 도움이 됩니다.\n**********\n " +
                        " 사용자 ID: %s\n 앱 버전 : %s\n 기기 : %s\n 안드로이드 OS 버전 : %d(%s)\n ",
                initialization.firebaseUser.uid,
                BuildConfig.VERSION_NAME,
                Build.DEVICE,
                Build.VERSION.SDK_INT,
                Build.VERSION.RELEASE
            )
        )
        email.setType("message/*")
        startActivity(email)
    }
}