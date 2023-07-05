package com.vocaengplus.vocaengplus.ui.setting


import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.vocaengplus.vocaengplus.adapter.AddCategoryListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityAddCategoryBinding
import com.vocaengplus.vocaengplus.databinding.AddvocaBinding
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.ui.util.Validation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCategoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding

    lateinit var firebaseUser: FirebaseUser
    lateinit var databaseref: DatabaseReference
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var uid: String
    lateinit var validation: Validation
    val initialization = Initialization
    lateinit var recyclerView: RecyclerView
    val wordlist = ArrayList<Voca>()
    var writername = ""
    var categoryname = ""
    var date = ""

    lateinit var addCategoryAdapter: AddCategoryListAdapter
    private val settingViewModel: SettingViewModel by viewModels()

    private val addWordDialog: AlertDialog by lazy {
        val dlgBinding = AddvocaBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setPositiveButton("추가") { _, _ ->
                val word = dlgBinding.editaddword.text.toString()
                val meaning = dlgBinding.editaddmeaning.text.toString()

                settingViewModel.addNewWord(word, meaning)
//

            }
            .setNegativeButton("취소") { _, _ ->
            }
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setCategoryData()
    }

    fun init() {

        addCategoryAdapter = AddCategoryListAdapter().apply {
            itemClickListener = object : AddCategoryListAdapter.OnItemClickListener {
                override fun OnDeleteClick(
                    holder: AddCategoryListAdapter.ViewHolder,
                    view: View,
                    position: Int,
                ) {
                    settingViewModel.removeNewWord(position)
                }
            }
        }

        binding.run {
            vm = settingViewModel
            lifecycleOwner = this@AddCategoryActivity
            wordListRecyclerView.adapter = addCategoryAdapter
        }

        binding.run {
            addWordButton.setOnClickListener {
                addWordDialog.show()
            }

            addWordListButton.setOnClickListener {
                settingViewModel.addNewWordList()
            }

            cancelButton.setOnClickListener {
                finish()
            }
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

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingViewModel.isSavedSuccess.collectLatest {
                    if (it) {
                        finish()
                    }
                }
            }
        }
    }

    private fun setCategoryData() {
        settingViewModel.getNewCategoryWriterAndDate()
    }
}