package com.vocaengplus.vocaengplus.adapter.bindingAdapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter


@BindingAdapter("isSelected")
fun isSelected(view: ImageView, isSelected: Int) {
    view.isSelected = isSelected == 1
}