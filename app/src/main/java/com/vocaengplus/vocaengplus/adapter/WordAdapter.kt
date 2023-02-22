package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.RowBinding
import com.vocaengplus.vocaengplus.model.data.Voca

class WordAdapter(options:FirebaseRecyclerOptions<Voca>):FirebaseRecyclerAdapter<Voca, WordAdapter.ViewHolder>(options) {

    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, position:Int)
        fun OnStarClick(holder: ViewHolder, view: View, position:Int)
        fun OnItemLongClick(holder: ViewHolder, position:Int):Boolean
    }
    var itemClickListener: OnItemClickListener?=null

    inner class ViewHolder(val binding: RowBinding): RecyclerView.ViewHolder(binding.root){
        val word: TextView =itemView.findViewById(R.id.word)
        val meaning: TextView =itemView.findViewById(R.id.meaning)
        val star: ImageView =itemView.findViewById(R.id.star)
        init{
            word.setOnClickListener{
                itemClickListener?.OnItemClick(this,adapterPosition)
            }
            word.setOnLongClickListener {
                itemClickListener?.OnItemLongClick(this,adapterPosition) == true
            }
            star.setOnClickListener {
                itemClickListener?.OnStarClick(this,it,adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=RowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: Voca) {
        holder.word.text=model.word
        holder.meaning.text=model.meaning
        holder.meaning.visibility= View.GONE
        if(model.checked==0){
            holder.star.isSelected=false
        }
        else if(model.checked==1){
            holder.star.isSelected=true
        }
    }

}