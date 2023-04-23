package com.vocaengplus.vocaengplus.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.vocaengplus.vocaengplus.R
import com.vocaengplus.vocaengplus.databinding.MainTitleBinding

class MainTitleView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private var binding: MainTitleBinding? = null
    private var numberText: String
    private var titleText: String

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = MainTitleBinding.inflate(inflater, this, true)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MainTitleView,
            0, 0
        ).apply {

            try {
                numberText = getString(R.styleable.MainTitleView_numberText).toString()
                titleText = getString(R.styleable.MainTitleView_titleText).toString()

                elevation=10f
                clipToPadding=false
                setBackgroundResource(R.drawable.main_title_background)

                binding?.run {
                    titleNumberTextView.text = numberText
                    titleTextView.text = titleText
                }
            } finally {
                recycle()
            }
        }
    }
}