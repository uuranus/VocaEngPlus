package com.vocaengplus.vocaengplus.ui.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vocaengplus.vocaengplus.databinding.FragmentTestBinding
import com.vocaengplus.vocaengplus.model.data.Voca
import java.util.*

class TestFragment(val category:String, val data: ArrayList<Voca>, val type:String) : Fragment() {
    var binding: FragmentTestBinding?=null
    lateinit var resultdata:ArrayList<Voca>
    val random= Random()
    var index=0
    var answer=0
    var corcount=0
    var wrongcount=0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentTestBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resultdata=ArrayList<Voca>()
        if(type=="영어 문제 - 한국어 답"){
            initData("e")
        }
        else{
            initData("k")
        }
    }

    private fun initData(type:String) {
        if(index==data.size){
            (activity as TestActivity).replaceFragment(TestResultFragment(category,wrongcount,corcount,resultdata),"testresult")
            return
        }

        binding!!.apply {
            if(type=="e"){
                questionTextView.text=data[index].word.toUpperCase()
                when(random.nextInt(3)){
                    0 ->{
                        radioButton.text=data[index].meaning
                        val str=getInCorrect(data[index].word,type)
                        radioButton2.text=str[0]
                        radioButton3.text=str[1]
                        answer=0
                    }
                    1 ->{
                        radioButton2.text=data[index].meaning
                        val str=getInCorrect(data[index].word,type)
                        radioButton.text=str[0]
                        radioButton3.text=str[1]
                        answer=1
                    }
                    2 ->{
                        radioButton3.text=data[index].meaning
                        val str=getInCorrect(data[index].word,type)
                        radioButton.text=str[0]
                        radioButton2.text=str[1]
                        answer=2
                    }
                }
            }
            else{
                questionTextView.text=data[index].meaning
                when (random.nextInt(3)) {
                    0 -> {
                        radioButton.text = data[index].word
                        val str = getInCorrect(data[index].meaning,type)
                        radioButton2.text = str[0]
                        radioButton3.text = str[1]
                        answer = 0
                    }
                    1 -> {
                        radioButton2.text = data[index].word
                        val str = getInCorrect(data[index].meaning,type)
                        radioButton.text = str[0]
                        radioButton3.text = str[1]
                        answer = 1
                    }
                    2 -> {
                        radioButton3.text = data[index].word
                        val str = getInCorrect(data[index].meaning,type)
                        radioButton.text = str[0]
                        radioButton2.text = str[1]
                        answer = 2
                    }
                }
            }

            radioButton.setOnClickListener {
                if(answer==0){
                    Toast.makeText(requireContext(),"정답입니다",Toast.LENGTH_SHORT).show()
                    corcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,1))

                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다",Toast.LENGTH_SHORT).show()
                    wrongcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,0))

                }
                clearBtn()
                ++index
                initData(type)
            }

            radioButton2.setOnClickListener {
                if(answer==1){
                    Toast.makeText(requireContext(),"정답입니다",Toast.LENGTH_SHORT).show()
                    corcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,1))

                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다",Toast.LENGTH_SHORT).show()
                    wrongcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,0))

                }
                clearBtn()
                ++index
                initData(type)
            }
            radioButton3.setOnClickListener {
                if(answer==2){
                    Toast.makeText(requireContext(),"정답입니다",Toast.LENGTH_SHORT).show()
                    corcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,1))

                }
                else{
                    Toast.makeText(requireContext(),"틀렸습니다",Toast.LENGTH_SHORT).show()
                    wrongcount++
                    resultdata.add(Voca(data[index].category,data[index].word,data[index].meaning,0))

                }
                clearBtn()
                ++index
                initData(type)
            }
        }
    }

    fun getInCorrect(engkor:String,type:String):ArrayList<String>{
        val str=ArrayList<String>()
        if(type=="e"){
            while(str.size!=2){
                val tempdata=data.get(random.nextInt(data.size))
                if(tempdata.word.equals(engkor)||tempdata.meaning in str){
                    continue
                }
                else{
                    str.add(tempdata.meaning)
                }
            }
        }
        else{
            while(str.size!=2){
                val tempdata=data.get(random.nextInt(data.size))
                if(tempdata.meaning.equals(engkor)||tempdata.word in str){
                    continue
                }
                else{
                    str.add(tempdata.word)
                }
            }
        }
        return str
    }

    fun clearBtn(){
        binding!!.radioButton.isChecked=false
        binding!!.radioButton2.isChecked=false
        binding!!.radioButton3.isChecked=false
    }

    override fun onDestroy() {
        super.onDestroy()
        binding=null
    }
}