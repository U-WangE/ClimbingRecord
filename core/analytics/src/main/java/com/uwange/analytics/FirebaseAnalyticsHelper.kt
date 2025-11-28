package com.uwange.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
): AnalyticsHelper() {
    override fun logEvent(event: AnalyticsEvent) {
        firebaseAnalytics.logEvent(event.type, event.properties)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }
}