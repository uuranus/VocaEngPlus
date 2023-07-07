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
import com.vocaengplus.vocaengplus.adapter.WordAdapter
import com.vocaengplus.vocaengplus.adapter.WordAdapterListener
import com.vocaengplus.vocaengplus.databinding.ActivityWordBinding
import com.vocaengplus.vocaengplus.databinding.AddvocaBinding
import com.vocaengplus.vocaengplus.databinding.WordhelpBinding
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.model.data.newData.Word
import com.vocaengplus.vocaengplus.view.WordDialogFragment
import com.vocaengplus.vocaengplus.view.WordDialogListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

    var isSelected = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

        wordViewModel.getData()
    }

    private fun init() {
        adapter = WordAdapter(object : WordAdapterListener {
            override fun onStartClick(word: Word) {
                wordViewModel.setMyWord(word, word.checked)
            }

            override fun onItemLongClick(word: Word) {
                editVocaAlertDialog.showDialog(supportFragmentManager, "WordDialogFragment")
            }
        })

        binding.run {

            vm = wordViewModel
            lifecycleOwner = this@WordActivity

            helpButton.setOnClickListener {
                helpAlertDialog.show()
            }

            addVocaButton.setOnClickListener {
                addVocaAlertDialog.show()
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

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                }

            wordRecyclerView.adapter = adapter
            adapter.submitList(emptyList())
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                wordViewModel.wordListNames.collectLatest {
                    binding.wordSpinner.adapter =
                        ArrayAdapter(
                            this@WordActivity,
                            R.layout.support_simple_spinner_dropdown_item,
                            it
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

    override fun onDialogPositiveClick(dialog: WordDialogFragment, voca: Voca) {
        wordViewModel.editVoca(voca)
    }

    override fun onDialogNegativeClick(dialog: WordDialogFragment) {
        dialog.dismiss()
    }

}