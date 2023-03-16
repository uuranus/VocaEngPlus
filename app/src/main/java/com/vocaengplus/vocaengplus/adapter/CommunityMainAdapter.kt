package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.di.Initialization
import com.vocaengplus.vocaengplus.model.data.CommunityCategory

class CommunityMainAdapter(val items: ArrayList<CommunityCategory>) :
    RecyclerView.Adapter<CommunityMainAdapter.ViewHolder>() {

    val initialization = Initialization

    interface OnItemClickListener {
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleTextView)
        val more: ImageButton = itemView.findViewById(R.id.goDetailButton)
        val writer: TextView = itemView.findViewById(R.id.writerTextView)
        val writer_profile: ImageView = itemView.findViewById(R.id.profileImageView)
        val upload: TextView = itemView.findViewById(R.id.uploadDateTextView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val likeicon: ImageView = itemView.findViewById(R.id.likeImageView)
        val like: TextView = itemView.findViewById(R.id.likeTextView)
        val downloadicon: ImageView = itemView.findViewById(R.id.downloadImageView)
        val download: TextView = itemView.findViewById(R.id.downloadTextView)

        init {
            more.setOnClickListener {
                itemClickListener?.OnItemClick(this, it, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_community_main, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].categoryname
        holder.writer.text = "${items[position].categorywriter}"
        initialization.firebaseStorage.child(items[position].profile).downloadUrl.addOnSuccessListener {

            Glide.with(holder.writer_profile)
                .load(it)
                .circleCrop()
                .into(holder.writer_profile)
        }
        holder.upload.text = "업로드  ${items[position].uploadDate}"
        holder.description.text = items[position].description
        if (items[position].isLiked) {
            holder.likeicon.isSelected = true
        } else {
            holder.likeicon.isSelected = false
        }
        holder.like.text = "${items[position].like}"
        if (items[position].isDownloaded) {
            holder.downloadicon.isSelected = true
        } else {
            holder.downloadicon.isSelected = false
        }
        holder.download.text = "${items[position].download}"
    }

}