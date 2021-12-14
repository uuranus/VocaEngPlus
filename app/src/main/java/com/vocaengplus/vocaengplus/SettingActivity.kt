package com.vocaengplus.vocaengplus

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vocaengplus.vocaengplus.databinding.ActivitySettingBinding
import com.vocaengplus.vocaengplus.databinding.EditcategoryBinding
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding
    lateinit var firebaseauth : FirebaseAuth    // 파이어베이스 인증객체
    lateinit var databaseref : DatabaseReference    // 실시간 데이터베이스
    lateinit var firebaseUser:FirebaseUser
    lateinit var uid:String
    val initialization= Initialization
    lateinit var validation: Validation
    lateinit var recyclerView:RecyclerView
    lateinit var adapter:CategoryAdapter
    lateinit var date:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    fun init(){
        firebaseauth= initialization.getFBauth()
        databaseref=initialization.getDBref()
        firebaseUser=initialization.getFBuser()
        uid=initialization.getuid()
        validation= Validation()
        date=initialization.getdate()

        initRecyclerView()

        binding.addcategorybtn.setOnClickListener {
            val intent= Intent(this@SettingActivity,AddCategory::class.java)
            startActivityForResult(intent,100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==100){
            if(resultCode== RESULT_OK){
                initRecyclerView()
            }
        }
    }

    fun  initRecyclerView(){
        recyclerView=binding.recyclerview
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        val data=ArrayList<Category>()
        //단어장 목록 가져와서 recyclerView에 출력
        databaseref.child("UserData")
                .child(uid.toString())
                .child("downloadData")
                .orderByKey()
                .get().addOnSuccessListener {
                    for(c in it.children){
                        if(c.key.toString()=="오답노트"){
                            continue
                        }
                        data.add(Category(c.child("categoryname").value.toString(),c.child("categorywriter").value.toString(),c.child("categorywritertoken").value.toString(),c.child("downloadDate").value.toString(),c.child("description").value.toString(),ArrayList<Voca>())) //단어목록은 안 쓸 거기 때문에
                    }
                    adapter = CategoryAdapter(data)

                    adapter.itemClickListener=object: CategoryAdapter.OnItemClickListener {
                        override fun OnDeleteClick(holder: CategoryAdapter.ViewHolder, position: Int) {
                            val categoryname=holder.categoryname.text.toString()
                            deleteAlert(categoryname)
                        }

                        override fun OnEditClick(
                            holder: CategoryAdapter.ViewHolder,
                            position: Int
                        ) {
                            editAlert(holder)
                        }

                    }
                    recyclerView.adapter=adapter
                    if(data.size==0){
                        binding.emptyrecyclervie.visibility= View.VISIBLE
                    }
                    else{
                        binding.emptyrecyclervie.visibility= View.GONE
                    }
                }
    }
    private fun editAlert(holder:CategoryAdapter.ViewHolder){
        val dlgBinding= EditcategoryBinding.inflate(layoutInflater)
        dlgBinding.editcategoryname.setText(holder.categoryname.text.toString())
        dlgBinding.editcategorydescription.setText(holder.description.text.toString().substring(5,holder.description.text.toString().length))
        val builder= AlertDialog.Builder(this)
        builder.setView(dlgBinding.root)
            .setPositiveButton("네"){
                    _,_ ->
                val newcategoryname=dlgBinding.editcategoryname.text.toString()
                val newdescription=dlgBinding.editcategorydescription.text.toString()
                if(!(validation.checkInput(arrayOf(newcategoryname)))) {
                    Toast.makeText(this@SettingActivity,"단어장 이름을 입력해주세요",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if(!(validation.isValidateCategoryName(newcategoryname))) {
                    Toast.makeText(this,"단어장 이름은 1~20글자여야 합니다.(.#$[] 제외)",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if(!(validation.checkInput(arrayOf(newdescription)))) {
                    Toast.makeText(this@SettingActivity,"단어장 내용 입력해주세요",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                if(newdescription.length>40) {
                    Toast.makeText(this,"단어장 내용은 1~40자 이내여야 합니다",Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                //업데이트
                databaseref.child("UserData").child(uid).child("downloadData").child(holder.categoryname.text.toString()).get().addOnSuccessListener {
                    val data=it.getValue(CategoryForEdit::class.java)
                    data!!.categoryname=newcategoryname
                    data!!.description=newdescription
                    databaseref.child("UserData").child(uid).child("downloadData").child(holder.categoryname.text.toString()).removeValue()
                    databaseref.child("UserData").child(uid).child("downloadData").child(newcategoryname).setValue(data)
                    databaseref.child("UserData").child(uid).child("downloadNames").child(holder.categoryname.text.toString()).removeValue()
                    databaseref.child("UserData").child(uid).child("downloadNames").child(newcategoryname).setValue(newcategoryname)
                    initRecyclerView()
                }
            }
            .setNegativeButton("아니오") {
                _,_ ->

            }

        val dlg=builder.create()
        dlg.show()

    }

    private fun deleteAlert(categoryname:String){
        val builder= AlertDialog.Builder(this)
        builder.setMessage("해당 단어장을 삭제하시겠습니까?")
            .setPositiveButton("네"){
                    _,_ ->

                databaseref.child("UserData")
                    .child(uid.toString())
                    .child("downloadData")
                    .child(categoryname)
                    .removeValue()

                databaseref.child("UserData")
                    .child(firebaseUser.uid.toString())
                    .child("downloadNames")
                    .child(categoryname)
                    .removeValue()

                databaseref.child("UserLog")
                    .child(firebaseUser.uid.toString()).get().addOnSuccessListener {
                        if(it.hasChild(date.substring(0,7))){
                            var count=it.child(date.substring(0,7))
                                .child("AddDelete")
                                .child("deleteCategory").value.toString().toInt()

                            databaseref.child("UserLog")
                                .child(firebaseUser.uid.toString())
                                .child(date.substring(0,7))
                                .child("AddDelete")
                                .child("deleteCategory").setValue(++count)

                        }
                        else{
                            initialization.initData("deleteCategory")
                        }
                        Toast.makeText(this@SettingActivity,"$categoryname 단어장 삭제 완료",Toast.LENGTH_SHORT).show()
                        initRecyclerView()
                }
            }
            .setNegativeButton("아니오"){
                    _,_ ->
            }
        val dlg=builder.create()
        dlg.show()

    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        finish()
    }

}