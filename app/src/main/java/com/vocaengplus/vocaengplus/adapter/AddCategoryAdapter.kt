package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.Voca

class AddCategoryAdapter(val wordlist: ArrayList<Voca>): RecyclerView.Adapter<AddCategoryAdapter.ViewHolder>() {

    interface OnItemClickListener{
        fun OnDeleteClick(holder: ViewHolder, view: View, position:Int)
    }
    var itemClickListener: OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordnum=itemView.findViewById<TextView>(R.id.wordnum)
        val wordeng=itemView.findViewById<TextView>(R.id.wordeng)
        val wordmeaning=itemView.findViewById<TextView>(R.id.wordmeaning)
        val deletebtn=itemView.findViewById<ImageButton>(R.id.deleteword)
        init{
            deletebtn.setOnClickListener{
                itemClickListener?.OnDeleteClick(this,it,adapterPosition)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.item_setting_word,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.wordnum.text=(position+1).toString()
        holder.wordeng.text=wordlist[position].word
        holder.wordmeaning.text=wordlist[position].meaning
    }

    override fun getItemCount(): Int {
        return wordlist.size
    }



}