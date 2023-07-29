package com.vocaengplus.vocaengplus.ui.search

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.DialogAddSearchWordBinding
import com.vocaengplus.vocaengplus.model.data.Word
import com.vocaengplus.vocaengplus.model.data.WordList

interface SearchAddDialogListener {
    fun onWordListClickListener(position: Int)
    fun onPositiveClickListener()
    fun onNegativeClickListener()
}

class SearchAddDialog(val listener: SearchAddDialogListener) : DialogFragment() {
    private lateinit var binding: DialogAddSearchWordBinding
    private var currentWord: Word? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DialogAddSearchWordBinding.inflate(layoutInflater)

        binding.wordListSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long,
                ) {
                    listener.onWordListClickListener(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton("추가") { _, _ ->
                listener.onPositiveClickListener()
            }
            .setNegativeButton("취소") { _, _ ->
                dismiss()
            }.create()
    }

    fun showDialog(manager: FragmentManager, word: Word) {
        show(manager, tag)
        currentWord = word
    }

    fun setWordList(wordList: List<WordList>) {
        binding.wordListSpinner.adapter =
            ArrayAdapter(
                requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                wordList.map { it2 -> it2.wordListName }
            )
    }

    override fun onStart() {
        super.onStart()
        setCurrentWord()
    }

    private fun setCurrentWord() {
        binding.run {
            wordEditText.setText(currentWord?.word)
            meaningEditText.setText(currentWord?.meaning)
        }
    }
}