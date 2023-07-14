package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemWordListBinding
import com.vocaengplus.vocaengplus.model.data.newData.WordList
import com.vocaengplus.vocaengplus.ui.util.toDateString

class SettingListAdapter : ListAdapter<WordList, SettingListAdapter.ViewHolder>(diffUtil) {
    interface OnItemClickListener {
        fun onDeleteClick(position: Int)
        fun onEditClick(position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemWordListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                deleteButton.setOnClickListener {
                    itemClickListener?.onDeleteClick(adapterPosition)
                }
                editButton.setOnClickListener {
                    itemClickListener?.onEditClick(adapterPosition)
                }
            }
        }

        fun bind(data: WordList) {
            binding.run {
                titleTextView.text = data.wordListName
                writerTextView.text = "작성자 : ${data.writerName}"
                downloadDateTextView.text = "다운로드 날짜 : ${data.downLoadDate.toDateString()}"
                descriptionTextView.text = "내용 : ${data.description}"
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<WordList>() {
            override fun areItemsTheSame(oldItem: WordList, newItem: WordList): Boolean {
                return oldItem.writerUid == newItem.writerUid
            }

            override fun areContentsTheSame(oldItem: WordList, newItem: WordList): Boolean {
                return oldItem == newItem
            }

        }
    }
}