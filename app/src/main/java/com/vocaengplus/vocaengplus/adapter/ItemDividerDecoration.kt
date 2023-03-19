package com.vocaengplus.vocaengplus.adapter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ItemDividerDecoration(
    private val dividerColor: Int? = null,
    private val top: Int,
    private val start: Int,
    private val end: Int,
    private val bottom: Int
) :
    RecyclerView.ItemDecoration() {

    private val mPaint = Paint().apply {
        dividerColor?.let {
            color = it
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildLayoutPosition(view)
        if (position == 0) {
            outRect.bottom = bottom
            outRect.right = end
        } else if (position == parent.childCount - 1) {
            outRect.top = top
            outRect.left = start
        } else {
            outRect.left = start
            outRect.right = end
            outRect.bottom = bottom
            outRect.top = top
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        dividerColor?.let {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)

                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + 1

                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
            }
        }

    }
}