package com.vocaengplus.vocaengplus


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
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.databinding.ActivityWordBinding
import com.vocaengplus.vocaengplus.databinding.AddvocaBinding
import com.vocaengplus.vocaengplus.databinding.EditvocaBinding
import com.vocaengplus.vocaengplus.databinding.WordhelpBinding

class WordActivity : AppCompatActivity() {
    lateinit var binding: ActivityWordBinding
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: WordAdapter
    lateinit var firebaseauth:FirebaseAuth
    lateinit var databaseref:DatabaseReference
    lateinit var firebaseUser : FirebaseUser
    lateinit var uid:String
    lateinit var category:String
    lateinit var date:String
    lateinit var validation: Validation
    val initialization= Initialization
    var categories=ArrayList<String>()
    var isSelected=0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding= ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        validation= Validation()

        firebaseauth= initialization.getFBauth()
        firebaseUser=initialization.getFBuser()
        databaseref=initialization.getDBref()
        uid=initialization.getuid()
        date=initialization.getdate()

        databaseref.child("UserData").child(firebaseUser.uid.toString()).child("downloadNames").get().addOnSuccessListener {
            for(child in it.children){
                if(child.value=="오답노트"){
                    continue
                }
                categories.add(child.value.toString())
            }
            category= categories[isSelected]

            binding.spinner.adapter= ArrayAdapter(this, R.layout.simple_dropdown_item_1line,categories)
            binding.spinner.setSelection(isSelected)

            initRecyclerView()
        }

        binding.spinner.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category=categories.get(position)
                isSelected=position
                initRecyclerView()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //TODO("Not yet implemented")
            }

        }

        binding.helpbtn.setOnClickListener {
            val dlgBinding= WordhelpBinding.inflate(layoutInflater)
            val builder= AlertDialog.Builder(this)
            builder.setView(dlgBinding.root)
                .setNeutralButton("확인"){
                        _,_ ->{

                }
                }
            val dlg=builder.create()
            dlg.show()
        }

        binding.addvocabtn.setOnClickListener {
            val dlgBinding= AddvocaBinding.inflate(layoutInflater)
            val dlgbuilder= AlertDialog.Builder(this)
            dlgbuilder.setView(dlgBinding.root)
                .setPositiveButton("추가"){
                        _,_ ->

                    val word=dlgBinding.editaddword.text.toString()
                    val meaning=dlgBinding.editaddmeaning.text.toString()

                    if(word.length==0||meaning.length==0){
                        Toast.makeText(this,"단어 추가 실패", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if(!validation.isValidateWord(word)){
                            Toast.makeText(this,"영단어 입력이 올바르지 않아 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else if(!validation.isValidateMeaning(meaning)){
                            Toast.makeText(this,"뜻 입력이 올바르지 않아 추가에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                        else{

                             databaseref.child("UserData")
                                            .child(uid)
                                            .child("downloadData")
                                            .child(category)
                                            .child("words")
                                            .child(word)
                                            .setValue(Voca(category, word, meaning, 0))

                            //log 추가
                            databaseref.child("UserLog")
                                    .child(uid).get().addOnSuccessListener {
                                               if (it.hasChild(date.substring(0, 7))) {
                                                    var add = it.child(date.substring(0, 7))
                                                        .child("AddDelete")
                                                        .child("addWord").value.toString().toInt()

                                                    databaseref.child("UserLog")
                                                        .child(uid)
                                                        .child(date.substring(0, 7))
                                                        .child("AddDelete")
                                                        .child("addWord").setValue(++add)

                                               } else {
                                                    initialization.initData("addWord")
                                               }
                                    }
                            adapter.notifyDataSetChanged()

                        }

                    }

                }
                .setNegativeButton("취소"){
                        _,_ ->
                }
            val dlg=dlgbuilder.create()
            dlg.show()
        }
    }

    private fun initRecyclerView() {
        recyclerView=binding.recyclerview
        recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

        //해당단어장 단어 가져와서 recyclerView에 출력
        val query = databaseref.child("UserData")
                .child(uid)
                .child("downloadData")
                .child(category)
                .child("words")
                .orderByChild("word")

        val option = FirebaseRecyclerOptions.Builder<Voca>()
                .setQuery(query, Voca::class.java)
                .build()
        adapter = WordAdapter(option)

        adapter.itemClickListener=object: WordAdapter.OnItemClickListener {
            override fun OnItemClick(
                    holder: WordAdapter.ViewHolder,
                    position: Int
            ) {
                if(holder.meaning.visibility== View.VISIBLE){
                    holder.meaning.visibility= View.GONE
                }
                else {
                    holder.meaning.visibility= View.VISIBLE
                }
            }

            override fun OnStarClick(
                holder: WordAdapter.ViewHolder,
                view: View,
                position: Int
            ) {
                if(!view.isSelected) { //즐겨찾기 되어있지 않은 별이었다면
                    view.isSelected = true
                    databaseref.child("UserData")
                            .child(uid)
                            .child("downloadData")
                            .child(category)
                            .child("words")
                            .child(holder.word.text.toString())
                            .child("checked")
                            .setValue(1)
                }
                else{
                    view.isSelected=false
                    databaseref.child("UserData")
                            .child(uid)
                            .child("downloadData")
                            .child(category)
                            .child("words")
                            .child(holder.word.text.toString())
                            .child("checked")
                            .setValue(0)
                }

            }

            override fun OnItemLongClick(
                holder: WordAdapter.ViewHolder,
                position: Int
            ): Boolean {
                editAlertDlg(holder)
                return true
            }

        }
        recyclerView.adapter=adapter

        val simpleCallback=object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
            ItemTouchHelper.LEFT){
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

        adapter.startListening()

    }


    fun editAlertDlg(viewHolder: RecyclerView.ViewHolder){
        val dlgBinding= EditvocaBinding.inflate(layoutInflater)
        val position=viewHolder.adapterPosition
        dlgBinding.editaddword.setText(adapter.getItem(position).word)
        dlgBinding.editaddmeaning.setText(adapter.getItem(position).meaning)
        val oldword=adapter.getItem(position).word
        val isChecked=adapter.getItem(position).checked

        val builder= AlertDialog.Builder(this)
        builder.setView(dlgBinding.root)
            .setPositiveButton("네"){
                    _,_ ->
                val word=dlgBinding.editaddword.text.toString()
                val meaning=dlgBinding.editaddmeaning.text.toString()

                if(word.length==0||meaning.length==0){
                    Toast.makeText(this,"단어 수정 실패",Toast.LENGTH_SHORT).show()
                }
                else {
                    if (!validation.isValidateWord(word)) {
                        Toast.makeText(this, "영단어 입력이 올바르지 않아 수정에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!validation.isValidateMeaning(meaning)) {
                        Toast.makeText(this, "뜻 입력이 올바르지 않아 수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    } else {
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
                                    .setValue(Voca(category, word, meaning, isChecked))
                    }
                }
            }
            .setNegativeButton("아니오"){
                    _,_ ->
            }
        val dlg=builder.create()
        dlg.show()
    }

    fun deleteAlertDlg(viewHolder: RecyclerView.ViewHolder){
        val position=viewHolder.adapterPosition
        val word=adapter.getItem(position).word
        val builder=AlertDialog.Builder(this)
        builder.setMessage("해당 단어를 삭제하시겠습니까?")
                .setPositiveButton("네"){
                    _,_ ->
                    databaseref.child("UserData")
                            .child(uid)
                            .child("downloadData")
                            .child(category)
                            .child("words")
                            .child(word)
                            .removeValue()

                    //단어 삭제 log
                    databaseref.child("UserLog")
                            .child(uid).get().addOnSuccessListener {
                                if(it.hasChild(date.substring(0,7))){
                                    var count=it.child(date.substring(0,7))
                                            .child("AddDelete")
                                            .child("deleteWord").value.toString().toInt()

                                    databaseref.child("UserLog")
                                               .child(uid)
                                               .child(date.substring(0,7))
                                                .child("AddDelete")
                                                .child("deleteWord").setValue(++count)
                                }
                                else{
                                    initialization.initData("deleteWord")
                                }
                                adapter.notifyDataSetChanged()
                            }

                }
                .setNegativeButton("아니오"){
                    _,_ ->
                    initRecyclerView()
                }
        val dlg=builder.create()
        dlg.show()
        dlg.setOnDismissListener {
            initRecyclerView()
        }
    }

    override fun onBackPressed() {
        setResult(isSelected)
        finish()
    }


}