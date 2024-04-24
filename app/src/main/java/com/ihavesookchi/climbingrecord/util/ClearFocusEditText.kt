package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText


class ClearFocusEditText: AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttribute: Int) : super(
        context,
        attributeSet,
        defStyleAttribute
    )

    override fun onKeyPreIme(keyCode: Int, keyEvent: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.action == KeyEvent.ACTION_UP) clearFocus()
        return super.onKeyPreIme(keyCode, keyEvent)
    }
}