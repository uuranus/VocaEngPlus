package com.vocaengplus.vocaengplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CommunityMyAdapter(val items:ArrayList<CommunityForUpload>,val uploads:ArrayList<String>): RecyclerView.Adapter<CommunityMyAdapter.ViewHolder>() {

    val initialization= Initialization

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.communitymytitle)
        val upload: ImageButton = itemView.findViewById(R.id.communitymybtn)
        val description: TextView = itemView.findViewById(R.id.communitymydescription)

        init {
            upload.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityMyAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.communitymyrow, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: CommunityMyAdapter.ViewHolder, position: Int) {
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