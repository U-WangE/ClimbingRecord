package com.uwange.navigation

import kotlinx.serialization.Serializable

sealed interface Route

@Serializable
data object DashBoardBaseRoute : Route

sealed interface DashBoardGraph : Route {

}