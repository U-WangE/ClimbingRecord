package com.uwange.analytics

import androidx.navigation.NavDestination
import com.uwange.navigation.AuthGraph

fun NavDestination.getRouteName(): String? = this.route?.let { mapRouteToName(it) }

private fun mapRouteToName(route: String): String? = when {
    route.startsWith(AuthGraph.LoginRout::class.qualifiedName.orEmpty()) -> "login_intro"
    else -> null
}