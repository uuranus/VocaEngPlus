package com.vocaengplus.vocaengplus.adapter.bindingAdapter

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vocaengplus.vocaengplus.model.data.newData.TestResult


@BindingAdapter("isSelected")
fun isSelected(view: ImageView, isSelected: Boolean) {
    view.isSelected = isSelected
}

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(url: Uri?) {
    url ?: return

    Glide.with(this)
        .load(url)
        .circleCrop()
        .into(this)
}

@BindingAdapter("submitData")
fun <T> submitData(view: RecyclerView, data: List<T>?) {
    data ?: return

    val adapter = view.adapter as ListAdapter<T, RecyclerView.ViewHolder?>
    adapter.submitList(data)
}


@BindingAdapter("correctData")
fun setCorrectData(view: TextView, results: List<TestResult>) {
    view.text = "맞은 개수 : ${results.count { it.isCorrect }}개"
}

@BindingAdapter("inCorrectData")
fun setInCorrectData(view: TextView, results: List<TestResult>) {
    view.text = "틀린 개수 : ${results.count { it.isCorrect.not() }}개"
}