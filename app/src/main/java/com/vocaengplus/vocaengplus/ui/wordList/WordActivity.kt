package com.vocaengplus.vocaengplus.ui.wordList

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.adapter.WordAdapter
import com.vocaengplus.vocaengplus.adapter.WordAdapterListener
import com.vocaengplus.vocaengplus.databinding.ActivityWordBinding
import com.vocaengplus.vocaengplus.databinding.AddvocaBinding
import com.vocaengplus.vocaengplus.databinding.WordhelpBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.ui.util.Validation
import com.vocaengplus.vocaengplus.view.WordDialogFragment
import com.vocaengplus.vocaengplus.view.WordDialogListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WordActivity : AppCompatActivity(), WordDialogListener {
    private lateinit var binding: ActivityWordBinding
    private val wordViewModel: WordViewModel by viewModels()
    private lateinit var adapter: WordAdapter

    private val helpAlertDialog: AlertDialog by lazy {
        val dlgBinding = WordhelpBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setNeutralButton("확인") { _, _ ->
            }
            .create()
    }
    private val addVocaAlertDialog: AlertDialog by lazy {
        val dlgBinding = AddvocaBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setPositiveButton("추가") { _, _ ->
                val word = dlgBinding.editaddword.text.toString()
                val meaning = dlgBinding.editaddmeaning.text.toString()

                wordViewModel.addNewVoca(word, meaning)

            }
            .setNegativeButton("취소") { _, _ ->
            }
            .create()
    }

    private val editVocaAlertDialog: WordDialogFragment = WordDialogFragment(this)

    var categories = ArrayList<String>()
    var isSelected = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        adapter = WordAdapter(object : WordAdapterListener {
            override fun onStartClick(voca: Voca) {
                wordViewModel.setMyWord(voca, voca.checked)
            }

            override fun onItemLongClick(voca: Voca) {
                editVocaAlertDialog.showWordDialog(supportFragmentManager, "WordDialogFragment")
            }
        })

        binding.run {

            helpButton.setOnClickListener {
                helpAlertDialog.show()
            }

            addVocaButton.setOnClickListener {
                addVocaAlertDialog.show()
            }

            //spinner
            wordSpinner.adapter =
                ArrayAdapter(this@WordActivity,
                    R.layout.support_simple_spinner_dropdown_item,
                    categories)
            wordSpinner.setSelection(0)

            //getData()
            wordRecyclerView.adapter = adapter
        }

//        databaseref.child("UserData").child(firebaseUser.uid).child("downloadNames")
//            .get().addOnSuccessListener {
//                for (child in it.children) {
//                    if (child.value == "오답노트") {
//                        continue
//                    }
//                    categories.add(child.value.toString())
//                }
//                category = categories[isSelected]
//
//
//
//                initRecyclerView()
//            }
//
//        binding.wordSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                category = categories.get(position)
//                isSelected = position
//                initRecyclerView()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                //TODO("Not yet implemented")
//            }
//
//        }

    }

    override fun onStop() {
        super.onStop()
        //최근 단어장 저장
    }

//    private fun initRecyclerView() {
//        recyclerView = binding.wordRecyclerView
//
//
//        //해당단어장 단어 가져와서 recyclerView에 출력
//        val query = databaseref.child("UserData")
//            .child(uid)
//            .child("downloadData")
//            .child(category)
//            .child("words")
//            .orderByChild("word")
//
//        val option = FirebaseRecyclerOptions.Builder<Voca>()
//            .setQuery(query, Voca::class.java)
//            .build()
//        adapter = WordAdapter(option)
//
//        adapter.itemClickListener = object : WordAdapter.OnItemClickListener {
//            override fun OnItemClick(
//                holder: WordAdapter.ViewHolder,
//                position: Int
//            ) {
////                if (holder.meaning.visibility == View.VISIBLE) {
////                    holder.meaning.visibility = View.GONE
////                } else {
////                    holder.meaning.visibility = View.VISIBLE
////                }
//            }
//
//            override fun OnStarClick(
//                holder: WordAdapter.ViewHolder,
//                view: View,
//                position: Int
//            ) {
////                if (!view.isSelected) { //즐겨찾기 되어있지 않은 별이었다면
////                    view.isSelected = true
////                    databaseref.child("UserData")
////                        .child(uid)
////                        .child("downloadData")
////                        .child(category)
////                        .child("words")
////                        .child(holder.word.text.toString())
////                        .child("checked")
////                        .setValue(1)
////                } else {
////                    view.isSelected = false
////                    databaseref.child("UserData")
////                        .child(uid)
////                        .child("downloadData")
////                        .child(category)
////                        .child("words")
////                        .child(holder.word.text.toString())
////                        .child("checked")
////                        .setValue(0)
////                }
//
//            }
//
//            override fun OnItemLongClick(
//                holder: WordAdapter.ViewHolder,
//                position: Int
//            ): Boolean {
//                editAlertDlg(holder)
//                return true
//            }
//
//        }
//        recyclerView.adapter = adapter
//
//        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
//            ItemTouchHelper.DOWN or ItemTouchHelper.UP,
//            ItemTouchHelper.LEFT
//        ) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return true
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                deleteAlertDlg(viewHolder)
//            }
//
//        }
//        val itemTouchHelper = ItemTouchHelper(simpleCallback)
//        itemTouchHelper.attachToRecyclerView(recyclerView)
//
//        adapter.startListening()
//
//    }

//    fun deleteAlertDlg(viewHolder: RecyclerView.ViewHolder) {
//        val position = viewHolder.adapterPosition
//        val word = adapter.getItem(position).word
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("해당 단어를 삭제하시겠습니까?")
//            .setPositiveButton("네") { _, _ ->
//                databaseref.child("UserData")
//                    .child(uid)
//                    .child("downloadData")
//                    .child(category)
//                    .child("words")
//                    .child(word)
//                    .removeValue()
//
//                //단어 삭제 log
//                databaseref.child("UserLog")
//                    .child(uid).get().addOnSuccessListener {
//                        if (it.hasChild(date.substring(0, 7))) {
//                            var count = it.child(date.substring(0, 7))
//                                .child("AddDelete")
//                                .child("deleteWord").value.toString().toInt()
//
//                            databaseref.child("UserLog")
//                                .child(uid)
//                                .child(date.substring(0, 7))
//                                .child("AddDelete")
//                                .child("deleteWord").setValue(++count)
//                        } else {
//                            initialization.initData("deleteWord")
//                        }
//                        adapter.notifyDataSetChanged()
//                    }
//
//            }
//            .setNegativeButton("아니오") { _, _ ->
//                initRecyclerView()
//            }
//        val dlg = builder.create()
//        dlg.show()
//        dlg.setOnDismissListener {
//            initRecyclerView()
//        }
//    }

    override fun onBackPressed() {
        setResult(isSelected)
        finish()
    }


    override fun onDialogPositiveClick(dialog: WordDialogFragment, voca: Voca) {
        wordViewModel.editVoca(voca)
    }

    override fun onDialogNegativeClick(dialog: WordDialogFragment) {
        dialog.dismiss()
    }

}