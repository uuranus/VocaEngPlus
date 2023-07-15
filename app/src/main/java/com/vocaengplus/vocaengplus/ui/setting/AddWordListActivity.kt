package com.vocaengplus.vocaengplus.ui.setting


import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.adapter.AddCategoryListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityAddCategoryBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.view.WordDialogFragment
import com.vocaengplus.vocaengplus.view.WordDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddWordListActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBinding
    lateinit var addCategoryAdapter: AddCategoryListAdapter
    private val addWordListViewModel: AddWordListViewModel by viewModels()

    private val addWordDialog = WordDialogFragment(object : WordDialogListener {
        override fun onDialogPositiveClick(dialog: WordDialogFragment, word: Word) {
            addWordListViewModel.addNewWord(word)
        }

        override fun onDialogNegativeClick(dialog: WordDialogFragment) {
        }

    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setWordListData()
    }

    fun init() {

        addCategoryAdapter = AddCategoryListAdapter().apply {
            itemClickListener = object : AddCategoryListAdapter.OnItemClickListener {
                override fun onDeleteClick(position: Int) {
                    addWordListViewModel.removeNewWord(position)
                }
            }
        }

        binding.run {
            vm = addWordListViewModel
            lifecycleOwner = this@AddWordListActivity
            wordListRecyclerView.adapter = addCategoryAdapter
        }

        binding.run {
            addWordButton.setOnClickListener {
                addWordDialog.showDialog(supportFragmentManager)
            }

            addWordListButton.setOnClickListener {
                addWordListViewModel.addNewWordList()
            }

            cancelButton.setOnClickListener {
                finish()
            }
        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addWordListViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addWordListViewModel.isSavedSuccess.collectLatest {
                    if (it) {
                        finish()
                    }
                }
            }
        }
    }

    private fun setWordListData() {
        addWordListViewModel.getWriterAndDate()
    }
}