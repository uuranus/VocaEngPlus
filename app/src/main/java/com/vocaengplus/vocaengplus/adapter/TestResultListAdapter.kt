package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.databinding.ItemTestResultBinding
import com.vocaengplus.vocaengplus.model.data.newData.TestResult

class TestResultListAdapter() :
    ListAdapter<TestResult, TestResultListAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(val binding: ItemTestResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TestResult) {
            binding.countTextView.text = "${adapterPosition + 1}"
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemTestResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<TestResult>() {
            override fun areItemsTheSame(oldItem: TestResult, newItem: TestResult): Boolean {
                return oldItem.answer == newItem.answer
            }

            override fun areContentsTheSame(oldItem: TestResult, newItem: TestResult): Boolean {
                return oldItem == newItem
            }

        }
    }
}