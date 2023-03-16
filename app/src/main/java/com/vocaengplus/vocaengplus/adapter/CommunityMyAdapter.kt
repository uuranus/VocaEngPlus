package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.CommunityForUpload

class CommunityMyAdapter(val items:ArrayList<CommunityForUpload>, val uploads:ArrayList<String>): RecyclerView.Adapter<CommunityMyAdapter.ViewHolder>() {

    val initialization= Initialization

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val upload: ImageButton = itemView.findViewById(R.id.uploadButton)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)

        init {
            upload.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_community_my, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].categoryname
        holder.description.text = items[position].description
        if(uploads.contains(items[position].categoryname)){
            holder.upload.isSelected=true
        }
        else{
            holder.upload.isSelected=false
        }
    }

}