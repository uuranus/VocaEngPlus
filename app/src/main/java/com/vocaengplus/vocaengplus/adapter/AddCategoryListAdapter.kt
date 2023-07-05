package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemSettingWordBinding
import com.vocaengplus.vocaengplus.model.data.newData.Word

class AddCategoryListAdapter : ListAdapter<Word, AddCategoryListAdapter.ViewHolder>(diffUtil) {

    interface OnItemClickListener {
        fun OnDeleteClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null


    inner class ViewHolder(val binding: ItemSettingWordBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.deleteword.setOnClickListener {
                itemClickListener?.OnDeleteClick(this, it, adapterPosition)
            }
        }

        fun bind(data: Word) {
            binding.run {
                wordnum.text = (adapterPosition + 1).toString()
                wordeng.text = data.word
                wordmeaning.text = data.meaning
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSettingWordBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
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