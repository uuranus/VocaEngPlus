package com.vocaengplus.vocaengplus.ui.test

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.databinding.FragmentTestMainBinding
import com.vocaengplus.vocaengplus.databinding.TesthelpBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Voca
import java.util.Random

class TestMainFragment : Fragment() {

    var binding: FragmentTestMainBinding?=null

    lateinit var firebaseauth:FirebaseAuth
    lateinit var databaseref: DatabaseReference
    lateinit var firebaseUser : FirebaseUser
    lateinit var uid:String
    val initialization= Initialization

    lateinit var category:String
    lateinit var categories:ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding= FragmentTestMainBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseauth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        databaseref= initialization.getDBref()
        uid=initialization.getuid()

        binding!!.helpButton.setOnClickListener {
            val dlgBinding= TesthelpBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(requireContext())
            builder.setView(dlgBinding.root)
                    .setNeutralButton("확인"){
                        _,_ ->{

                    }
                    }
            val dlg=builder.create()
            dlg.show()
        }

        val testtypes=arrayOf("영어 문제 - 한국어 답","한국어 문제 - 영어 답")
        binding!!.testTypeSpinner.adapter= ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line,testtypes)
        binding!!.testTypeSpinner.setSelection(0)


        categories=ArrayList<String>()
        databaseref.child("UserData").child(uid).child("downloadNames").get().addOnSuccessListener {
            for(child in it.children){
                categories.add(child.value.toString())
            }
            category= categories[0]

            binding!!.testCategorySpinner.adapter= ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line,categories)
            binding!!.testCategorySpinner.setSelection(0)

            binding!!.teststartbtn.setOnClickListener {   //단어장 목록을 가져와야 테스트 시작 가능
                val testcategory=binding!!.testCategorySpinner.selectedItem.toString()
                val testtype=binding!!.testCategorySpinner.selectedItem.toString()

                databaseref.child("UserData").child(uid.toString()).child("downloadData").child(testcategory).child("words")
                    .get().addOnSuccessListener {snapshot->
                        if(snapshot.childrenCount<3){
                            Toast.makeText(requireContext(),"테스트할 단어 수가 부족합니다", Toast.LENGTH_SHORT).show()
                            return@addOnSuccessListener
                        }
                        else{
                            getRandomData(snapshot.childrenCount.toInt(), testcategory, testtype, snapshot)
                            return@addOnSuccessListener
                        }
                    }

            }
        }
    }

    private fun getRandomData(count:Int, testcategory:String,testtype:String,snapshot:DataSnapshot){
        val data=ArrayList<Voca>()
        val random= Random()
        val randomdata= ArrayList<Int>()
        var size=10

        if(count<10){
            size=count
            for(i in 0 until size){
                data.add(Voca(" "," "," ",0))
                randomdata.add(0)
            }
        }
        else {
            for (i in 0 until 10) {
                data.add(Voca(" ", " ", " ", 0))
                randomdata.add(0)
            }
        }

        //랜덤 값 10개 저장
        var i=0
        while(i<size){
            randomdata[i]=random.nextInt(size)
            for(j in 0 until i){
                if(randomdata[i]==randomdata[j]){
                    i--
                }
            }
            i++
        }

        var curchild=0
        for(c in snapshot.children){
            for(i in 0 until size) {
                if (randomdata[i] == curchild) {
                    data[i] = Voca(c.child("category").value.toString(), c.child("word").value.toString(), c.child("meaning").value.toString(), c.child("checked").value.toString().toInt())
                }
            }
            curchild++

        }

        (activity as TestActivity).replaceFragment(TestFragment(testcategory, data, testtype), "test")

    }
}