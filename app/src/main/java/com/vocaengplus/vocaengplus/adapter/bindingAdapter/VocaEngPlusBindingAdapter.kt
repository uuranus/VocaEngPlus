package com.vocaengplus.vocaengplus.adapter.bindingAdapter

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.model.data.TestResult


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

@BindingAdapter("setProfileImageUrl")
fun ImageView.setProfileImageUrl(url: Uri?) {

    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.default1)
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

@BindingAdapter("translateWord")
fun translateWord(view: TextView, isKorToEng: Boolean) {
    view.text = if (isKorToEng) "한국어" else "영어"
}

@BindingAdapter("translatedWord")
fun translatedWord(view: TextView, isKorToEng: Boolean) {
    view.text = if (isKorToEng) "영어" else "한국어"
}