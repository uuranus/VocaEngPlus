package com.vocaengplus.vocaengplus.ui.wordList

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.adapter.WordAdapterListener
import com.vocaengplus.vocaengplus.adapter.WordListAdapter
import com.vocaengplus.vocaengplus.databinding.ActivityWordBinding
import com.vocaengplus.vocaengplus.databinding.HelpWordBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.view.WordDialogFragment
import com.vocaengplus.vocaengplus.view.WordDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordBinding
    private val wordViewModel: WordViewModel by viewModels()
    private lateinit var adapter: WordListAdapter

    private val helpAlertDialog: AlertDialog by lazy {
        val dlgBinding = HelpWordBinding.inflate(layoutInflater)
        AlertDialog.Builder(this)
            .setView(dlgBinding.root)
            .setNeutralButton("확인") { _, _ ->
            }
            .create()
    }

    private val editOrDeleteAlertDialog: AlertDialog by lazy {
        val items = arrayOf("수정", "삭제")
        AlertDialog.Builder(this)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        val selectWord = wordViewModel.getSelectedWord()
                        editWordAlertDialog.showDialog(supportFragmentManager, selectWord)
                    }

                    1 -> {
                        val selectWord = wordViewModel.getSelectedWord()
                        wordViewModel.deleteWord(selectWord)
                    }

                    else -> {}
                }
            }
            .create()
    }

    private val addWordAlertDialog: WordDialogFragment =
        WordDialogFragment(object : WordDialogListener {
            override fun onDialogPositiveClick(dialog: WordDialogFragment, word: Word) {
                wordViewModel.addWord(word)
            }

            override fun onDialogNegativeClick(dialog: WordDialogFragment) {
            }
        })

    private val editWordAlertDialog: WordDialogFragment =
        WordDialogFragment(object : WordDialogListener {
            override fun onDialogPositiveClick(dialog: WordDialogFragment, word: Word) {
                wordViewModel.editWord(word)
            }

            override fun onDialogNegativeClick(dialog: WordDialogFragment) {

            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        wordViewModel.getData()
    }

    private fun init() {
        adapter = WordListAdapter().apply {
            itemClickListener = object : WordAdapterListener {
                override fun onStarClick(word: Word, position: Int) {
                    wordViewModel.setMyWord(position)
                }

                override fun onItemLongClick(word: Word, position: Int) {
                    wordViewModel.selectWord(position)
                    editOrDeleteAlertDialog.show()
                }
            }
        }

        binding.run {

            vm = wordViewModel
            lifecycleOwner = this@WordActivity

            helpButton.setOnClickListener {
                helpAlertDialog.show()
            }

            addVocaButton.setOnClickListener {
                addWordAlertDialog.showDialog(supportFragmentManager)
            }

            binding.wordSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        wordViewModel.selectWordList(position)
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) = Unit

                }

            wordRecyclerView.adapter = adapter
            adapter.submitList(emptyList())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                wordViewModel.wordLists.collectLatest {
                    binding.wordSpinner.adapter =
                        ArrayAdapter(
                            this@WordActivity,
                            R.layout.support_simple_spinner_dropdown_item,
                            it.map { it2 -> it2.wordListName }
                        )
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                wordViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        //TODO 최근 단어장 저장
    }
}