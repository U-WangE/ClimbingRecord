package com.uwange.analytics

import android.os.Bundle

data class AnalyticsEvent(
    val type: String,
    val properties: Bundle? = null
) {
    object Type {
        const val SCREEN_VIEW = "screen_view"
        const val BUTTON_CLICK = "button_click"
        const val ACTION = "action"
    }

    object PropertiesKeys {
        const val SCREEN_NAME = "screen_name"
        const val BUTTON_NAME = "button_name"
        const val ACTION_NAME = "action_name"
    }
}