package com.uwange.auth.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.uwange.navigation.AuthGraph
import com.uwange.navigation.AuthGraphBaseRoute

fun NavGraphBuilder.authNavGraph() {
    navigation<AuthGraphBaseRoute>(startDestination = AuthGraph.LoginRout) {
        composable<AuthGraph.LoginRout> {
//            LoginRout()
        }
    }
}