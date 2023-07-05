package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemWordBinding
import com.vocaengplus.vocaengplus.model.data.Voca
import com.vocaengplus.vocaengplus.model.data.newData.Word
import javax.inject.Inject

interface WordAdapterListener {
    fun onStartClick(word: Word)
    fun onItemLongClick(word: Word)
}

class WordAdapter @Inject constructor(private val listener: WordAdapterListener) :
    ListAdapter<Word, WordAdapter.WordViewHolder>(diffUtil) {

    inner class WordViewHolder(val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                engWordTextView.setOnClickListener {
                    meanWordTextView.isVisible = meanWordTextView.isVisible.not()
                }
                engWordTextView.setOnLongClickListener {
                    listener.onItemLongClick(currentList[adapterPosition])
                    return@setOnLongClickListener false
                }
                starImageView.setOnClickListener {
                    listener.onStartClick(currentList[adapterPosition])
                }
            }
        }

        fun bind(word: Word) {
            binding.item = word
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WordViewHolder(view)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem.word == newItem.word
            }

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem == newItem
            }

        }
    }

}