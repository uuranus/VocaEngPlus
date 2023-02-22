package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.adapter.TestResultAdapter
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.di.Initialization
import java.util.*

class TestResultFragment(val testcategory:String,val wrongcount:Int, val corcount:Int,val resultdata:ArrayList<Voca>) : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: TestResultAdapter
    lateinit var firebaseauth:FirebaseAuth
    lateinit var databaseref: DatabaseReference
    lateinit var firebaseUser : FirebaseUser
    lateinit var uid:String
    val initialization= Initialization

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tc=view.findViewById<TextView>(R.id.totalcount)
        val cc=view.findViewById<TextView>(R.id.corcount)
        val wc=view.findViewById<TextView>(R.id.wrongcount)

        tc.text="총 개수 : ${wrongcount+corcount}"
        cc.text="맞은 개수 : $corcount"
        wc.text="틀린 개수 : $wrongcount"

        recyclerView=view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        adapter= TestResultAdapter(resultdata)

        recyclerView.adapter=adapter

        val btn=view.findViewById<Button>(R.id.endtestbtn)
        btn.setOnClickListener {
            firebaseauth = initialization.getFBauth()
            firebaseUser = initialization.getFBuser()
            databaseref =initialization.getDBref()
            uid=initialization.getuid()

            val date = initialization.getdate()
            val month = date.substring(0, 7)

            databaseref.child("UserLog").child(uid).get().addOnSuccessListener { snapshot ->
                if (!snapshot.hasChild(date.substring(0, 7))) {
                    initData()
                } else {
                    var testcount = snapshot.child(date.substring(0, 7)).child("Test").child("testcount").value.toString().toInt()
                    var tc = snapshot.child(date.substring(0, 7)).child("Test").child("totalcount").value.toString().toInt()
                    var cc = snapshot.child(date.substring(0, 7)).child("Test").child("corcount").value.toString().toInt()
                    var wc = snapshot.child(date.substring(0, 7)).child("Test").child("wrongcount").value.toString().toInt()

                    //테스트 정보값 갱신
                    databaseref.child("UserLog").child(uid).child(month).child("Test").child("testcount").setValue(++testcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test").child("totalcount").setValue(tc + wrongcount + corcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test").child("corcount").setValue(cc + corcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test").child("wrongcount").setValue(wc + wrongcount)

                    initlog()
                }


                //틀린 단어들 오답노트 추가/삭제
                for(i in resultdata){
                    if(i.checked==0){ //오답이라면
                        if(testcategory!="오답노트"){
                            databaseref.child("UserData").child(uid.toString()).child("downloadData").child("오답노트").child("words")
                                    .child(i.category+" "+i.word).setValue(Voca(i.category,i.word,i.meaning,0))
                        }
                        //오답노트 아니면 그냥 pass
                    }
                    else if(i.checked==1){ //정답이라면
                       if(testcategory=="오답노트"){ //오답노트 시험이었다면 삭제
                           databaseref.child("UserData").child(uid.toString()).child("downloadData").child("오답노트").child("words")
                                   .child(i.category+" "+i.word).removeValue()
                       }
                        //오답노트 아니면 그냥 pass
                    }
                }

                //테스트 시작 화면으로 돌아가기
                Toast.makeText(requireContext(),"테스트가 종료되었습니다.",Toast.LENGTH_SHORT).show()
                (activity as TestActivity).replaceFragment(TestMainFragment(), "testmain")

            }
        }

    }

    private fun initData(){
        //해당 월의 내용 테스트 본 내용 반영해서 초기화
        var date=initialization.getdate()
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("Test").child("testcount").setValue(1)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("Test").child("totalcount").setValue(wrongcount+corcount)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("Test").child("corcount").setValue(corcount)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("Test").child("wrongcount").setValue(wrongcount)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("AddDelete").child("addCategory").setValue(0)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("AddDelete").child("deleteCategory").setValue(0)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("AddDelete").child("addWord").setValue(0)
        databaseref.child("UserLog").child(uid).child(date.substring(0,7)).child("AddDelete").child("deleteWord").setValue(0)
        initlog()
    }

    private fun initlog(){
        //log 값 저장
        val date = initialization.getdate()
        val month = date.substring(0, 7)
        databaseref.child("UserLog").child(uid).child(month).child("Test").get().addOnSuccessListener {
            if(it.hasChild("log")){
                if(it.child("log").hasChild(date)){
                    var testcount = it.child("log").child(date).child("testcount").value.toString().toInt()
                    var tc =  it.child("log").child(date).child("totalcount").value.toString().toInt()
                    var cc =  it.child("log").child(date).child("corcount").value.toString().toInt()
                    var wc =  it.child("log").child(date).child("wrongcount").value.toString().toInt()

                    //테스트 정보값 갱신
                    databaseref.child("UserLog").child(uid).child(month).child("Test")
                            .child("log").child(date).child("testcount").setValue(++testcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test")
                            .child("log").child(date).child("totalcount").setValue(tc + wrongcount + corcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test")
                            .child("log").child(date).child("corcount").setValue(cc + corcount)
                    databaseref.child("UserLog").child(uid).child(month).child("Test")
                            .child("log").child(date).child("wrongcount").setValue(wc + wrongcount)

                    return@addOnSuccessListener
                }
            }
            databaseref.child("UserLog").child(uid).child(month).child("Test").child("log").child(date)
                    .child("testcount").setValue(1)
            databaseref.child("UserLog").child(uid).child(month).child("Test").child("log").child(date)
                    .child("totalcount").setValue(wrongcount + corcount)
            databaseref.child("UserLog").child(uid).child(month).child("Test").child("log").child(date)
                    .child("corcount").setValue(corcount)
            databaseref.child("UserLog").child(uid).child(month).child("Test").child("log").child(date)
                    .child("wrongcount").setValue(wrongcount)

        }


    }

}