package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemWordBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word

interface WordAdapterListener {
    fun onStarClick(word: Word, position: Int)
    fun onItemLongClick(word: Word, position: Int)
}

class WordListAdapter() :
    ListAdapter<Word, WordListAdapter.WordViewHolder>(diffUtil) {

    var itemClickListener: WordAdapterListener? = null

    inner class WordViewHolder(val binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                engWordTextView.setOnClickListener {
                    meanWordTextView.isVisible = meanWordTextView.isVisible.not()
                }
                engWordTextView.setOnLongClickListener {
                    itemClickListener?.onItemLongClick(
                        currentList[adapterPosition],
                        adapterPosition
                    )
                    return@setOnLongClickListener false
                }
                starImageView.setOnClickListener {
                    itemClickListener?.onStarClick(currentList[adapterPosition], adapterPosition)
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