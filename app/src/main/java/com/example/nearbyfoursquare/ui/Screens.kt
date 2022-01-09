package com.example.nearbyfoursquare.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screens(val route: String, val arguments: List<NamedNavArgument>) {
    object Places : Screens(route = "places", emptyList())
    object Details : Screens(
        route = "details",
        arguments = listOf(navArgument("placeId") { type = NavType.StringType })
    )
}