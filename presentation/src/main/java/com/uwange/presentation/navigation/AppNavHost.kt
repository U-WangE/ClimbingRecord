package com.uwange.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.uwange.auth.navigation.authNavGraph
import com.uwange.debug.analyticsNavigation
import com.uwange.navigation.AuthGraphBaseRoute

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthGraphBaseRoute,
        modifier = modifier
    ) {
        authNavGraph()
        analyticsNavigation()
    }
}