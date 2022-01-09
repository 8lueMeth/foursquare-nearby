package com.example.nearbyfoursquare.data.remote.model

data class Results(
    val fsq_id: String,
    val categories: List<Categories>?,
    val distance: Int,
    val location: Location,
    val name: String,
)