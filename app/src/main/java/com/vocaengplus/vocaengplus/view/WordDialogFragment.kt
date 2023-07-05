package com.vocaengplus.vocaengplus.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vocaengplus.vocaengplus.databinding.ItemWordBinding
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.model.data.newData.Word

interface WordDialogListener {
    fun onDialogPositiveClick(dialog: WordDialogFragment, voca: Voca)
    fun onDialogNegativeClick(dialog: WordDialogFragment)
}

class WordDialogFragment(private val listener: WordDialogListener) : DialogFragment() {

    private lateinit var wordItemBinding: ItemWordBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val inflater = requireActivity().layoutInflater
            wordItemBinding = ItemWordBinding.inflate(inflater)
            val builder = AlertDialog.Builder(it)
                .setView(wordItemBinding.root)
                .setPositiveButton("확인") { _, _ ->
                    listener.onDialogPositiveClick(this, Voca("", "", "", 0))
                }
                .setNegativeButton("취소") { _, _ ->
                    listener.onDialogNegativeClick(this)
                }
        }
        return super.onCreateDialog(savedInstanceState)
    }

    fun showDialog(manager: FragmentManager, tag: String) {
        wordItemBinding.run {
            engWordTextView.text = ""
            meanWordTextView.text = ""
        }
        show(manager, tag)
    }

    fun showDialog(word: Word, manager: FragmentManager, tag: String) {
        wordItemBinding.item = word
        show(manager, tag)
    }
}