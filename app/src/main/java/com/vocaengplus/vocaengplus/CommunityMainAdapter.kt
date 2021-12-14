package com.vocaengplus.vocaengplus

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CommunityMainAdapter(val items:ArrayList<CommunityCategory>): RecyclerView.Adapter<CommunityMainAdapter.ViewHolder>() {

        val initialization= Initialization

        interface OnItemClickListener {
            fun OnItemClick(holder: ViewHolder, view: View, position: Int)
        }

        var itemClickListener: OnItemClickListener? = null

        inner class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView) {
            val title: TextView = itemView.findViewById(R.id.communitymaintitle)
            val more: ImageButton = itemView.findViewById(R.id.communitymainbtn)
            val writer: TextView = itemView.findViewById(R.id.communitymainwriter)
            val writer_profile: ImageView = itemView.findViewById(R.id.communitymainwriterprofile)
            val upload: TextView = itemView.findViewById(R.id.communitymainupload)
            val description: TextView = itemView.findViewById(R.id.communitymaindescription)
            val likeicon:ImageView=itemView.findViewById(R.id.communitymainlikeicon)
            val like: TextView = itemView.findViewById(R.id.communitymainlike)
            val downloadicon:ImageView=itemView.findViewById(R.id.communitymaindownloadicon)
            val download: TextView = itemView.findViewById(R.id.communitymaindownload)

            init {
                more.setOnClickListener {
                    itemClickListener?.OnItemClick(this, it, adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommunityMainAdapter.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.communitymainrow, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: CommunityMainAdapter.ViewHolder, position: Int) {
            holder.title.text = items[position].categoryname
            holder.writer.text ="${items[position].categorywriter}"
            initialization.firebaseStorage.child(items[position].profile).downloadUrl.addOnSuccessListener {

                Glide.with(holder.writer_profile)
                        .load(it)
                        .circleCrop()
                        .into(holder.writer_profile)
            }
            holder.upload.text = "업로드  ${items[position].uploadDate}"
            holder.description.text = items[position].description
            if(items[position].isLiked){
                holder.likeicon.isSelected=true
            }
            else{
                holder.likeicon.isSelected=false
            }
            holder.like.text = "${items[position].like}"
            if(items[position].isDownloaded){
                holder.downloadicon.isSelected=true
            }
            else{
                holder.downloadicon.isSelected=false
            }
            holder.download.text = "${items[position].download}"
        }

    }