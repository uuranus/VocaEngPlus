package com.vocaengplus.vocaengplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityDetailAdapter(val items: CommunityCategory): RecyclerView.Adapter<CommunityDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val num: TextView = itemView.findViewById(R.id.communitydetailwordnum)
        val word: TextView = itemView.findViewById(R.id.communitydetailword)
        val meaning: TextView = itemView.findViewById(R.id.communitydetailmeaning)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityDetailAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.communitydetailrow, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.words.size
    }

    override fun onBindViewHolder(holder: CommunityDetailAdapter.ViewHolder, position: Int) {
        holder.num.text =(position+1).toString()
        holder.word.text =items.words[position].word
        holder.meaning.text = items.words[position].meaning
    }
}