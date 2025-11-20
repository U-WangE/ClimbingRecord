package com.uwange.analytics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.uwange.analytics.AnalyticsEvent.PropertiesKeys.ACTION_NAME
import com.uwange.analytics.AnalyticsEvent.PropertiesKeys.BUTTON_NAME
import com.uwange.analytics.AnalyticsEvent.PropertiesKeys.SCREEN_NAME
import com.uwange.analytics.AnalyticsEvent.Type.ACTION
import com.uwange.analytics.AnalyticsEvent.Type.BUTTON_CLICK
import com.uwange.analytics.AnalyticsEvent.Type.SCREEN_VIEW

abstract class AnalyticsHelper {
    abstract fun logEvent(event: AnalyticsEvent)
    abstract fun setUserId(userId: String?)

    fun trackClickEvent(
        screenName: String,
        buttonName: String,
        properties: MutableMap<String, Any?>? = null
    ) {
        val eventProperties = mutableMapOf<String, Any?>(
            SCREEN_NAME to screenName,
            BUTTON_NAME to buttonName
        ).apply {
            properties?.let { putAll(it) }
        }

        logEvent(
            AnalyticsEvent(
                type = BUTTON_CLICK,
                properties = eventProperties
            )
        )
    }

    fun trackActionEvent(
        screenName: String,
        actionName: String,
        properties: MutableMap<String, Any?>? = null
    ) {
        val eventProperties = mutableMapOf<String, Any?>(
            SCREEN_NAME to screenName,
            ACTION_NAME to actionName
        ).apply {
            properties?.let { putAll(it) }
        }

        logEvent(
            AnalyticsEvent(
                type = ACTION,
                properties = eventProperties
            )
        )
    }

    fun clearUserId() = setUserId(null)
}

class NoOpAnalyticsHelper: AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) = Unit
    override fun setUserId(userId: String?) = Unit
}

val LocalAnalyticsHelper = staticCompositionLocalOf<AnalyticsHelper> {
    NoOpAnalyticsHelper()
}

@Composable
fun TrackScreenViewEvent(
    key: Any? = Unit,
    screenName: String?,
    params: Map<String, Any?> = emptyMap(),
    analyticsHelper: AnalyticsHelper = LocalAnalyticsHelper.current
) = LaunchedEffect(key) {
    if (screenName != null) {
        val properties = mutableMapOf<String, Any?>(
            SCREEN_NAME to screenName
        ).apply {
            if (params.isNotEmpty()) {
                putAll(params)
            }
        }

        analyticsHelper.logEvent(
            AnalyticsEvent(
                type = SCREEN_VIEW,
                properties
            )
        )
    }
}

@Composable
fun TrackNavigationDestination(navController: NavHostController) {
    val analyticsHelper = LocalAnalyticsHelper.current

    LifecycleStartEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            val screenName = destination.getRouteName()
            if (screenName != null) {
                analyticsHelper.logEvent(
                    AnalyticsEvent(
                        type = SCREEN_VIEW,
                        properties = mutableMapOf(SCREEN_NAME to screenName)
                    )
                )
            }
        }

        navController.addOnDestinationChangedListener(listener)
        onStopOrDispose { navController.removeOnDestinationChangedListener(listener) }
    }
}