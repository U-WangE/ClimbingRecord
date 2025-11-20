package com.uwange.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FirebaseAnalyticsHelper @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics
): AnalyticsHelper() {
}