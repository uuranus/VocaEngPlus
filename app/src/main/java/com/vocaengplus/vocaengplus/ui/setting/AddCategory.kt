package com.vocaengplus.vocaengplus.ui.setting


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.adapter.AddCategoryAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityAddCategoryBinding
import com.vocaengplus.vocaengplus.databinding.AddvocaBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Category
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.ui.util.Validation
import java.util.*

class AddCategory : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    lateinit var firebaseUser:FirebaseUser
    lateinit var databaseref:DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var uid:String
    lateinit var validation: Validation
    val initialization= Initialization
    lateinit var adapter: AddCategoryAdapter
    lateinit var recyclerView:RecyclerView
    val wordlist=ArrayList<Voca>()
    var writername=""
    var categoryname=""
    var date=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        validation= Validation()

        firebaseAuth= Initialization.getFBauth()
        firebaseUser= Initialization.getFBuser()
        databaseref= Initialization.getDBref()
        uid= Initialization.getuid()

        binding.writer.text="작성자  ${firebaseUser.displayName}"
        date= Initialization.getdate()
        binding.addDate.text="추가 날짜  ${date}"


        recyclerView=binding.vocalist
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        adapter= AddCategoryAdapter(wordlist)
        adapter.itemClickListener=object: AddCategoryAdapter.OnItemClickListener {
            override fun OnDeleteClick(holder: AddCategoryAdapter.ViewHolder, view: View, position: Int) {
                wordlist.removeAt(position)
                adapter.notifyDataSetChanged()
            }
        }
        binding.vocalist.adapter=adapter


        binding.addbtn.setOnClickListener {
            val dlgBinding= AddvocaBinding.inflate(layoutInflater)
            val dlgbuilder= AlertDialog.Builder(this)
            dlgbuilder.setView(dlgBinding.root)
                    .setPositiveButton("추가"){
                        _,_ ->
                        val word=dlgBinding.editaddword.text.toString()
                        val meaning=dlgBinding.editaddmeaning.text.toString()

                        if(word.length==0||meaning.length==0){
                            Toast.makeText(this,"단어 추가 실패", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }
                        else if(!validation.isValidateWord(word)){
                            Toast.makeText(this,"영단어 입력이 올바르지 않아 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }
                        else if(!validation.isValidateMeaning(meaning)){
                            Toast.makeText(this,"뜻 입력이 올바르지 않아 추가에 실패했습니다.", Toast.LENGTH_SHORT).show()
                            return@setPositiveButton
                        }
                        else{
                            wordlist.add(Voca(categoryname,word,meaning,0))
                            adapter.notifyDataSetChanged()
                        }
                    }
                    .setNegativeButton("취소"){
                        _,_ ->
                    }
            val dlg=dlgbuilder.create()
            dlg.show()
        }

        binding.addokbtn.setOnClickListener {
            categoryname=binding.editnewcategoryname.text.toString()
            val description=binding.editnewcategorydescription.text.toString()

            if(categoryname.length==0){
                Toast.makeText(this,"단어장 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(!validation.isValidateCategoryName(categoryname)){
                Toast.makeText(this,"단어장 이름은 1~20글자여야 합니다.(.#$[] 제외)",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(description.length==0){
                Toast.makeText(this,"단어장 내용을 입력해주세요",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(description.length>40){
                Toast.makeText(this,"단어장 내용은 1~40자 이내여야 합니다",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if(wordlist.size==0){
                Toast.makeText(this,"단어가 하나 이상 존재해야 합니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //데이터베이스에 추가
            databaseref.child("UserData").child(uid.toString())
                    .child("downloadNames").child(categoryname).setValue(categoryname)
            databaseref.child("UserData").child(uid.toString())
                    .child("downloadData").child(categoryname).setValue(
                    Category(categoryname,
                        Initialization.getFBuser().displayName.toString(),
                        Initialization.uid,date,description,ArrayList<Voca>())
                )

            for(i in 0 until wordlist.size){
                databaseref.child("UserData").child(uid.toString())
                        .child("downloadData").child(categoryname).child("words").child(wordlist.get(i).word).setValue(
                        Voca(categoryname,wordlist.get(i).word,wordlist.get(i).meaning,0)
                    )
            }

            databaseref.child("UserLog")
                .child(uid.toString()).get().addOnSuccessListener {
                    if(it.hasChild(date.substring(0,7))){
                        var count=it.child(date.substring(0,7))
                                .child("AddDelete")
                                .child("addCategory").value.toString().toInt()

                         databaseref.child("UserLog")
                                 .child(firebaseUser.uid.toString())
                                 .child(date.substring(0,7))
                                 .child("AddDelete")
                                 .child("addCategory").setValue(++count)

                    }
                    else{
                        Initialization.initData("addCategory")
                    }
                        setResult(RESULT_OK)
                        finish()
                }
        }

        binding.addcancelbtn.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }


    }


}