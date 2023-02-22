package com.vocaengplus.vocaengplus.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.Voca
import java.util.*

class TestResultAdapter(val resultdata: ArrayList<Voca>): RecyclerView.Adapter<TestResultAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word=itemView.findViewById<TextView>(R.id.word)
        val meaning=itemView.findViewById<TextView>(R.id.meaning)
        val result=itemView.findViewById<ImageView>(R.id.result)
        var count=itemView.findViewById<TextView>(R.id.count)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.testresultrow,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.count.text=(position+1).toString()
        holder.word.text=resultdata[position].word
        holder.meaning.text=resultdata[position].meaning
        if(resultdata[position].checked==1){ //정답이라면
            holder.result.setImageResource(R.drawable.ic_o)
        }
        else{ //오답이라면
            holder.result.setImageResource(R.drawable.ic_x)
        }
    }

    override fun getItemCount(): Int {
        return resultdata.size
    }
}