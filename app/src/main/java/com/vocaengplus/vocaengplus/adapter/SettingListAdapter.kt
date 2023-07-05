package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemCategoryBinding
import com.vocaengplus.vocaengplus.model.data.newData.WordList

class SettingListAdapter : ListAdapter<WordList, SettingListAdapter.ViewHolder>(diffUtil) {
    interface OnItemClickListener {
        fun OnDeleteClick(holder: ViewHolder, position: Int)
        fun OnEditClick(holder: ViewHolder, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                deleteButton.setOnClickListener {
                    itemClickListener?.OnDeleteClick(this@ViewHolder, adapterPosition)
                }
                editButton.setOnClickListener {
                    itemClickListener?.OnEditClick(this@ViewHolder, adapterPosition)
                }
            }
        }

        fun bind(data: WordList) {
            binding.run {
                titleTextView.text = data.wordListName
                writerTextView.text = "작성자 : ${data.wordListWriter}"
                downloadDateTextView.text = "다운로드 날짜 : ${data.downLoadDate}"
                descriptionTextView.text = "내용 : ${data.description}"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<WordList>() {
            override fun areItemsTheSame(oldItem: WordList, newItem: WordList): Boolean {
                return oldItem.wordListWriterToken == newItem.wordListWriterToken
            }

            override fun areContentsTheSame(oldItem: WordList, newItem: WordList): Boolean {
                return oldItem == newItem
            }

        }
    }
}