package com.vocaengplus.vocaengplus.ui.setting

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.adapter.SettingListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivitySettingBinding
import com.vocaengplus.vocaengplus.databinding.EditcategoryBinding
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    private val settingAdapter = SettingListAdapter().apply {
        itemClickListener = object : SettingListAdapter.OnItemClickListener {
            override fun onDeleteClick( position: Int) {
                settingViewModel.selectWordList(position)
                deleteWordListDialog.show()
            }

            override fun onEditClick(position: Int) {
                settingViewModel.selectWordList(position)
                setOldWordList()
                editWordListDialog.show()
            }

        }
    }
    private val settingViewModel: SettingViewModel by viewModels()

    private val editDialogView: EditcategoryBinding by lazy {
        EditcategoryBinding.inflate(layoutInflater)
    }

    private val editWordListDialog: AlertDialog by lazy {
        AlertDialog.Builder(this).setView(editDialogView.root)
            .setPositiveButton("네") { _, _ ->
                val newCategoryName = editDialogView.editcategoryname.text.toString()
                val newDescription = editDialogView.editcategorydescription.text.toString()

                settingViewModel.editWordList(newCategoryName, newDescription)
            }
            .setNegativeButton("아니오") { _, _ ->

            }.create()
    }

    private val deleteWordListDialog: AlertDialog by lazy {
        AlertDialog.Builder(this).setMessage("해당 단어장을 삭제하시겠습니까?")
            .setPositiveButton("네") { _, _ ->
                settingViewModel.deleteWordList()
            }
            .setNegativeButton("아니오") { _, _ ->
            }.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init() {

        binding.run {
            vm = settingViewModel
            lifecycleOwner = this@SettingActivity
            myCategoryRecyclerView.adapter = settingAdapter
        }

        binding.addCategoryButton.setOnClickListener {
            val intent = Intent(this@SettingActivity, AddWordListActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        settingViewModel.getAllWordLists()
    }

    private fun setOldWordList() {
        val currentWordList = settingViewModel.getSelectedWordList()
        editDialogView.run {
            editcategoryname.setText(currentWordList.wordListName)
            editcategorydescription.setText(currentWordList.description)
        }
    }

}