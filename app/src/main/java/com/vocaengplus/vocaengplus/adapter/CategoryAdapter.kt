package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.CategoryrowBinding
import com.vocaengplus.vocaengplus.model.data.Category

class CategoryAdapter(val items: ArrayList<Category>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun OnDeleteClick(holder: ViewHolder, position: Int)
        fun OnEditClick(holder: ViewHolder, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class ViewHolder(val binding: CategoryrowBinding) : RecyclerView.ViewHolder(binding.root) {
        val categoryname: TextView = itemView.findViewById(R.id.categoryname)
        val categorywriter: TextView = itemView.findViewById(R.id.categorywriter)
        val downloaddate: TextView = itemView.findViewById(R.id.downloaddate)
        val description: TextView = itemView.findViewById(R.id.description)
        val editbtn:ImageButton=itemView.findViewById(R.id.editbtn)
        val deletebtn: ImageButton = itemView.findViewById(R.id.deletebtn)

        init {
            deletebtn.setOnClickListener {
                itemClickListener?.OnDeleteClick(this, adapterPosition)
            }
            editbtn.setOnClickListener {
                itemClickListener?.OnEditClick(this, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CategoryrowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.categoryname.text = items[position].categoryname
        holder.categorywriter.text = "작성자 : ${items[position].categorywriter}"
        holder.downloaddate.text = "다운로드 날짜 : ${items[position].downloadDate}"
        holder.description.text = "내용 : ${items[position].description}"
    }

    override fun getItemCount(): Int {
        return items.size
    }
}