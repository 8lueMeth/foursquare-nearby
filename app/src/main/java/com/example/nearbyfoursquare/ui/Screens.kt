package com.example.nearbyfoursquare.ui

sealed class Screens(val route: String) {
    object Places : Screens(route = "places")
    object Details : Screens(route = "details")
}