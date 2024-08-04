package com.ihavesookchi.climbingrecord.util.recyclerView

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.util.CommonUtil.dpToPx

/**
 * Recycler View 의 Item 간의 간격 Setting 하는 기능
 * orientation -> RecyclerView 의 orientation
 * itemSpacing -> Item 간의 간격
 * isStartEndSpacing -> 처음과 끝에 Spacing 을 넣을지 선택 하는 변수 (Default : true)
 **/
class ItemOffsetDecoration(
    private val orientation: Orientation,
    itemSpacing: Int = 0
): RecyclerView.ItemDecoration() {
    private var spacingItem: Int = itemSpacing.dpToPx()

    enum class Orientation {
        VERTICAL, HORIZONTAL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val applySpacing: (Int) -> Unit = { startSpacing ->
            when (orientation) {
                Orientation.VERTICAL -> {
                    outRect.top = startSpacing
                    outRect.bottom = startSpacing
                }
                Orientation.HORIZONTAL -> {
                    outRect.left = startSpacing
                    outRect.right = startSpacing
                }
            }
        }

        applySpacing(spacingItem)
    }
}