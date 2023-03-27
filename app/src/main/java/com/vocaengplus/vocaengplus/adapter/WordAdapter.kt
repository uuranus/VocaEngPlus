package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.vocaengplus.vocaengplus.databinding.ItemWordBinding
import com.vocaengplus.vocaengplus.model.data.Voca

class WordAdapter(options: FirebaseRecyclerOptions<Voca>) :
    FirebaseRecyclerAdapter<Voca, WordAdapter.ViewHolder>(options) {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, position: Int)
        fun OnStarClick(holder: ViewHolder, view: View, position: Int)
        fun OnItemLongClick(holder: ViewHolder, position: Int): Boolean
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.run {
                engWordTextView.setOnClickListener {
                    itemClickListener?.OnItemClick(this@ViewHolder, adapterPosition)
                }
                engWordTextView.setOnLongClickListener {
                    itemClickListener?.OnItemLongClick(this@ViewHolder, adapterPosition) == true
                }
                starImageView.setOnClickListener {
                    itemClickListener?.OnStarClick(this@ViewHolder, it, adapterPosition)
                }
            }

        }

        fun bind(voca: Voca) {
            binding.item = voca
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Voca) {
        holder.bind(model)
    }

}