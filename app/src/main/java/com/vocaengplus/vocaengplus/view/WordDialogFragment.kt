package com.vocaengplus.vocaengplus.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.vocaengplus.vocaengplus.databinding.DialogWordBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word

interface WordDialogListener {
    fun onDialogPositiveClick(dialog: WordDialogFragment, word: Word)
    fun onDialogNegativeClick(dialog: WordDialogFragment)
}

class WordDialogFragment(private val listener: WordDialogListener) : DialogFragment() {

    private lateinit var dialogWordBinding: DialogWordBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val inflater = requireActivity().layoutInflater
            dialogWordBinding = DialogWordBinding.inflate(inflater)
        }
        return AlertDialog.Builder(requireContext())
            .setView(dialogWordBinding.root)
            .setPositiveButton("확인") { _, _ ->
                listener.onDialogPositiveClick(
                    this,
                    Word(
                        "",
                        dialogWordBinding.wordEditText.text.toString(),
                        dialogWordBinding.meaningEditText.text.toString()
                    )
                )
            }
            .setNegativeButton("취소") { _, _ ->
                listener.onDialogNegativeClick(this)
            }
            .create()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogWordBinding.run {
            wordEditText.text.clear()
            meaningEditText.text.clear()
        }
    }

    fun setOldWord(word: Word) {
        dialogWordBinding.run {
            wordEditText.setText(word.word)
            meaningEditText.setText(word.meaning)
        }
    }

    fun showDialog(manager: FragmentManager) {
        show(manager, tag)
    }

}