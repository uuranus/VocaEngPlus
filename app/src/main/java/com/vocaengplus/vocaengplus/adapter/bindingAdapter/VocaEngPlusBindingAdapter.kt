package com.vocaengplus.vocaengplus.adapter.bindingAdapter

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


@BindingAdapter("isSelected")
fun isSelected(view: ImageView, isSelected: Int) {
    view.isSelected = isSelected == 1
}

@BindingAdapter("setImageUrl")
fun ImageView.setImageUrl(url: Uri?) {
    url ?: return

    Glide.with(this)
        .load(url)
        .circleCrop()
        .into(this)
}