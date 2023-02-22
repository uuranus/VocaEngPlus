package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {
    lateinit var binding: ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(TestMainFragment(),"testmain")
    }

    fun replaceFragment(fragment: Fragment, tag:String){
        if(supportFragmentManager.findFragmentByTag(tag)!=null){ //똑같은 화면 반복 출력일 경우
            return
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment,fragment,tag)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        //무조건 테스트 메인 화면으로 돌아가기
        if(supportFragmentManager.findFragmentByTag("testmain")==null){
            replaceFragment(TestMainFragment(),"testmain")
        }
        else { //지금이 테스트 메인 일때는 액티비티 종료
            super.onBackPressed()
        }
    }
}