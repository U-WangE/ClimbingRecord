package com.uwange.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uwange.analytics.TrackNavigationDestination
import com.uwange.navigation.Route
import kotlin.reflect.KClass

@Composable
internal fun rememberClimbingRecordAppState(
    navController: NavHostController = rememberNavController()
): ClimbingRecordAppState {
    TrackNavigationDestination(navController)
    return remember(navController, null, null) {
        ClimbingRecordAppState(
            navController = navController
        )
    }
}

@Stable
class ClimbingRecordAppState(
    val navController: NavHostController,
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

}

private fun NavDestination?.startsWithAnyRouteIn(routes: Set<KClass<out Route>>): Boolean =
    this?.hierarchy?.any { dest ->
        routes.any { route -> dest.route?.startsWith(route.qualifiedName ?: "") == true }
    } == true

fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean =
    this?.hierarchy?.any { it.hasRoute(route) } == true