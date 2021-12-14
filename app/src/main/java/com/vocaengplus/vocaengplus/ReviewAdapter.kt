package com.vocaengplus.vocaengplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ReviewAdapter(options: FirebaseRecyclerOptions<Voca>): FirebaseRecyclerAdapter<Voca, ReviewAdapter.ViewHolder>(options){

    interface OnItemClickListener{
        fun OnItemClick(holder:ViewHolder, view: View,  position:Int)
    }
    var itemClickListener:OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word: TextView =itemView.findViewById(R.id.word)
        val meaning:TextView =itemView.findViewById(R.id.meaning)
        val category: TextView =itemView.findViewById(R.id.category)
        init{
            word.setOnClickListener{
                itemClickListener?.OnItemClick(this,it,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.reviewrow,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int, model: Voca) {
        holder.word.text=model.word
        holder.category.text=model.category
        holder.meaning.text=model.meaning
    }


}