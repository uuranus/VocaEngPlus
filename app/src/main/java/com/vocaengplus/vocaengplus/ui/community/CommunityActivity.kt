package com.vocaengplus.vocaengplus.ui.community

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.ActivityCommunityBinding
import com.vocaengplus.vocaengplus.databinding.CommunityhelpBinding

class CommunityActivity : AppCompatActivity() {
    lateinit var binding: ActivityCommunityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(CommunityMainFramgent(),"communitymain")
        init()
    }

    private fun init(){
       binding.apply{
           helpbtn.setOnClickListener {
               val dlgBinding= CommunityhelpBinding.inflate(layoutInflater)
               val builder= AlertDialog.Builder(this@CommunityActivity)
               builder.setView(dlgBinding.root)
                       .setNeutralButton("확인"){
                           _,_ ->{

                       }
                       }
               val dlg=builder.create()
               dlg.show()
           }
           mybtn.setOnClickListener {
               replaceFragment(CommunityMyFragment(), "communitymy")
           }
       }
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
        //무조건 커뮤니티 메인 화면으로 돌아가기
        if(supportFragmentManager.findFragmentByTag("communitymain")==null){
            replaceFragment(CommunityMainFramgent(),"communitymain")
        }
        else { //지금이 커뮤니티 메인 일때는 액티비티 종료
            super.onBackPressed()
        }
    }
}