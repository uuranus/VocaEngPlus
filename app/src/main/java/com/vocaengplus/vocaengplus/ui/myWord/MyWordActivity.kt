package com.vocaengplus.vocaengplus.ui.myWord

import android.os.Bundle
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
import com.vocaengplus.vocaengplus.databinding.ActivityMyWordBinding
import com.vocaengplus.vocaengplus.databinding.HelpWordBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.view.WordDialogFragment
import com.vocaengplus.vocaengplus.view.WordDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyWordBinding
    private val myWordViewModel: MyWordViewModel by viewModels()
    private val wordListAdapter = WordListAdapter().apply {
        itemClickListener = object : WordAdapterListener {
            override fun onStarClick(word: Word, position: Int) {
                myWordViewModel.setMyWord(position)
            }

            override fun onItemLongClick(word: Word, position: Int) {
                myWordViewModel.selectWord(position)
                editOrDeleteAlertDialog.show()
            }

        }
    }

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
                        val selectWord = myWordViewModel.getSelectedWord()
                        editWordAlertDialog.showDialog(supportFragmentManager, selectWord)
                    }

                    1 -> {
                        val selectWord = myWordViewModel.getSelectedWord()
                        myWordViewModel.deleteWord(selectWord)
                    }

                    else -> {}
                }
            }
            .create()
    }

    private val editWordAlertDialog: WordDialogFragment =
        WordDialogFragment(object : WordDialogListener {
            override fun onDialogPositiveClick(dialog: WordDialogFragment, word: Word) {
                myWordViewModel.editWord(word)
            }

            override fun onDialogNegativeClick(dialog: WordDialogFragment) {

            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        myWordViewModel.getData()
    }

    fun init() {

        binding.run {
            vm = myWordViewModel
            lifecycleOwner = this@MyWordActivity
            wordRecyclerView.adapter = wordListAdapter
        }

        binding.helpButton.setOnClickListener {
            helpAlertDialog.show()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myWordViewModel.wordLists.collectLatest {
                    binding.wordSpinner.adapter =
                        ArrayAdapter(
                            this@MyWordActivity,
                            R.layout.support_simple_spinner_dropdown_item,
                            it.map { it2 -> it2.wordListName }
                        )
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                myWordViewModel.snackBarMessage.collectLatest {
                    if (it.isNotEmpty()) {
                        Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}

