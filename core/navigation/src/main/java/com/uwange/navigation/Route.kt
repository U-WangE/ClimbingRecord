package com.uwange.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object AuthGraphBaseRoute : Route

sealed interface AuthGraph : Route {
    @Serializable
    data object LoginRout : AuthGraph
}

@Serializable
data object DashBoardBaseRoute : Route

sealed interface DashBoardGraph : Route {

}