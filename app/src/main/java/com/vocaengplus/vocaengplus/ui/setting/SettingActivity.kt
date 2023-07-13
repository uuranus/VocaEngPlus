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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.vocaengplus.vocaengplus.adapter.SettingListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivitySettingBinding
import com.vocaengplus.vocaengplus.databinding.EditcategoryBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {
    lateinit var binding: ActivitySettingBinding

    private val settingAdapter = SettingListAdapter()
    private val settingViewModel: SettingViewModel by viewModels()

    private val editDlgView: EditcategoryBinding by lazy {
        EditcategoryBinding.inflate(layoutInflater).apply {
            editcategoryname.setText(holder.categoryname.text.toString())
            editcategorydescription.setText(
                holder.description.text.toString()
                    .substring(5, holder.description.text.toString().length)
            )
        }
    }
    private val editCategoryDialog: AlertDialog by lazy {
        AlertDialog.Builder(this).setView(editDlgView.root)
            .setPositiveButton("네") { _, _ ->
                val newCategoryName = editDlgView.editcategoryname.text.toString()
                val newDescription = editDlgView.editcategorydescription.text.toString()

                settingViewModel.editWordList(newCategoryName, newDescription)
            }
            .setNegativeButton("아니오") { _, _ ->

            }.create()
    }

    private val deleteCategoryDialog: AlertDialog by lazy {
        AlertDialog.Builder(this).setMessage("해당 단어장을 삭제하시겠습니까?")
            .setPositiveButton("네") { _, _ ->

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
                        if (it.hasChild(date.substring(0, 7))) {
                            var count = it.child(date.substring(0, 7))
                                .child("AddDelete")
                                .child("deleteCategory").value.toString().toInt()

                            databaseref.child("UserLog")
                                .child(firebaseUser.uid.toString())
                                .child(date.substring(0, 7))
                                .child("AddDelete")
                                .child("deleteCategory").setValue(++count)

                        } else {
                            initialization.initData("deleteCategory")
                        }
                        Toast.makeText(
                            this@SettingActivity,
                            "$categoryname 단어장 삭제 완료",
                            Toast.LENGTH_SHORT
                        ).show()
                        initRecyclerView()
                    }
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
            val intent = Intent(this@SettingActivity, AddCategoryActivity::class.java)
            startActivityForResult(intent, 100)
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

}