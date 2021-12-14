package com.vocaengplus.vocaengplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyWordAdapter(val items:ArrayList<Voca>): RecyclerView.Adapter<MyWordAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
        fun OnItemLongClick(holder: MyWordAdapter.ViewHolder, position: Int): Boolean
    }

    var itemClickListener: OnItemClickListener? = null


    inner class ViewHolder(val itemView:View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView = itemView.findViewById(R.id.word)
        val meaning: TextView = itemView.findViewById(R.id.meaning)
        val category: TextView = itemView.findViewById(R.id.category)

        init {
            word.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
            word.setOnLongClickListener {
                itemClickListener?.OnItemLongClick(this, adapterPosition) == true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWordAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mywordrow, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyWordAdapter.ViewHolder, position: Int) {
        holder.word.text = items[position].word
        holder.category.text = items[position].category
        holder.meaning.text = items[position].meaning
        holder.meaning.visibility=View.GONE
    }

}