package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.Voca

class MyWordAdapter(val items:ArrayList<Voca>): RecyclerView.Adapter<MyWordAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
        fun OnItemLongClick(holder: ViewHolder, position: Int): Boolean
    }

    var itemClickListener: OnItemClickListener? = null


    inner class ViewHolder(val itemView:View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView = itemView.findViewById(R.id.engWordTextView)
        val meaning: TextView = itemView.findViewById(R.id.meanWordTextView)
        val category: TextView = itemView.findViewById(R.id.categoryTextView)

        init {
            word.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
            word.setOnLongClickListener {
                itemClickListener?.OnItemLongClick(this, adapterPosition) == true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_my_word, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.word.text = items[position].word
        holder.category.text = items[position].category
        holder.meaning.text = items[position].meaning
        holder.meaning.visibility=View.GONE
    }

}