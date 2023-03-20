package com.vocaengplus.vocaengplus.ui.myWord

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.adapter.MyWordAdapter
import com.vocaengplus.vocaengplus.ui.util.Validation
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.databinding.ActivityMyWordBinding
import com.vocaengplus.vocaengplus.databinding.EditvocaBinding
import com.vocaengplus.vocaengplus.databinding.MywordhelpBinding
import com.vocaengplus.vocaengplus.di.Initialization

class MyWordActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyWordBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyWordAdapter
    lateinit var firebaseauth: FirebaseAuth
    lateinit var databaseref: DatabaseReference
    lateinit var firebaseUser : FirebaseUser
    lateinit var uid:String
    val initialization= Initialization
    lateinit var validation: Validation
    lateinit var category:String
    val data=ArrayList<Voca>()
    var categories=ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMyWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){
        validation= Validation
        firebaseauth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        databaseref= initialization.getDBref()
        uid=initialization.getuid()

        databaseref.child("UserData").child(uid.toString()).child("downloadNames").get().addOnSuccessListener {
            categories.add("전체")
            for(child in it.children){
                if(child.value=="오답노트"){
                    continue
                }
                categories.add(child.value.toString())
            }
            category= categories[0]

            binding.wordSpinner.adapter= ArrayAdapter(this, R.layout.simple_dropdown_item_1line,categories)
            binding.wordSpinner.setSelection(0)

        }

        binding.wordSpinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category=categories[position]
                initRecyclerView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }

        binding.helpButton.setOnClickListener {
            val dlgBinding= MywordhelpBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(dlgBinding.root)
                    .setNeutralButton("확인"){
                        _,_ ->{

                    }
                    }
            val dlg=builder.create()
            dlg.show()
        }

    }

    private fun initRecyclerView() {
        data.clear()
        recyclerView=binding.wordRecyclerView
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        //해당단어장 단어 가져와서 recyclerView에 출력
        if(category=="전체"){
            databaseref.child("UserData")
                    .child(uid.toString())
                    .child("downloadData").get().addOnSuccessListener {
                        for(c in it.children){
                            for(ch in c.child("words").children){
                                if(ch.child("checked").value.toString().toInt()==1){
                                    data.add(Voca(ch.child("category").value.toString(),ch.child("word").value.toString(),ch.child("meaning").value.toString(),1))
                                }
                            }
                        }
                        attachAdapter()
                }
        }
        else{
            databaseref.child("UserData")
                    .child(uid.toString())
                    .child("downloadData")
                    .child(category)
                    .child("words")
                    .orderByKey().get().addOnSuccessListener {
                        for(c in it.children){
                            if(c.child("checked").value.toString().toInt()==1){
                                data.add(Voca(c.child("category").value.toString(),c.child("word").value.toString(),c.child("meaning").value.toString(),1))
                            }
                        }
                        attachAdapter()
                }
        }

    }

    fun attachAdapter(){
        adapter = MyWordAdapter(data)

        adapter.itemClickListener=object: MyWordAdapter.OnItemClickListener {
            override fun OnItemClick(holder: MyWordAdapter.ViewHolder, view: View, position: Int) {
                if(holder.meaning.visibility==View.VISIBLE){
                    holder.meaning.visibility=View.GONE
                }
                else {
                    holder.meaning.visibility= View.VISIBLE
                }
            }

            override fun OnItemLongClick(holder: MyWordAdapter.ViewHolder, position: Int): Boolean {
                editAlertDlg(holder)
                return true
            }
        }

        recyclerView.adapter=adapter

        val simpleCallback=object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP, ItemTouchHelper.LEFT){
            override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteAlertDlg(viewHolder)
            }
        }
        val itemTouchHelper= ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        if(data.size==0){
            binding.emptyRecyclerTextView.visibility= View.VISIBLE
        }
        else{
            binding.emptyRecyclerTextView.visibility= View.GONE
        }
    }

    fun deleteAlertDlg(viewHolder: RecyclerView.ViewHolder){
        val word=data[viewHolder.adapterPosition].word

        val builder=AlertDialog.Builder(this)
        builder.setMessage("해당 단어의 즐겨찾기를 해제하시겠습니까?")
            .setPositiveButton("네"){
                    _,_ ->
                    databaseref.child("UserData")
                            .child(uid.toString())
                            .child("downloadData")
                            .child(data[viewHolder.adapterPosition].category)
                            .child("words")
                            .child(word)
                            .child("checked")
                            .setValue(0)
                     initRecyclerView()

            }
            .setNegativeButton("아니오"){
                    _,_ -> initRecyclerView()
            }
        val dlg=builder.create()
        dlg.show()
        dlg.setOnDismissListener {
            initRecyclerView()
        }
    }

    fun editAlertDlg(viewHolder: RecyclerView.ViewHolder){
        val dlgBinding= EditvocaBinding.inflate(layoutInflater)
        val position=viewHolder.adapterPosition
        dlgBinding.editaddword.setText(data[position].word)
        dlgBinding.editaddmeaning.setText(data[position].meaning)
        val oldword=data[position].word
        val isChecked=data[position].checked
        val category=data[position].category

        val builder= AlertDialog.Builder(this)
        builder.setView(dlgBinding.root)
                .setPositiveButton("네"){
                    _,_ ->
                    val word=dlgBinding.editaddword.text.toString()
                    val meaning=dlgBinding.editaddmeaning.text.toString()

                    if(word.length==0||meaning.length==0){ //수정 칸이 비어있다면
                        Toast.makeText(this,"단어 수정 실패",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if(!validation.isValidateWord(word)){
                            Toast.makeText(this,"영단어 입력이 올바르지 않아 수정에 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                        else if(!validation.isValidateMeaning(meaning)){
                            Toast.makeText(this,"뜻 입력이 올바르지 않아 수정에 실패했습니다.",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            //기존의 것 삭제하고
                            databaseref.child("UserData")
                                    .child(uid)
                                    .child("downloadData")
                                    .child(category)
                                    .child("words")
                                    .child(oldword)
                                    .removeValue()
                             //새로 추가
                            databaseref.child("UserData")
                                    .child(uid)
                                    .child("downloadData")
                                    .child(category)
                                    .child("words")
                                    .child(word)
                                    .setValue(Voca(category,word,meaning,isChecked))

                            data.removeAt(position)
                            data.add(position, Voca(category,word,meaning,isChecked))
                            adapter.notifyDataSetChanged()
                        }

                    }

                }
                .setNegativeButton("아니오"){
                    _,_ ->
                }
        val dlg=builder.create()
        dlg.show()
    }


}

