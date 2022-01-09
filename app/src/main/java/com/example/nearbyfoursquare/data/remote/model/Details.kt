package com.example.nearbyfoursquare.data.remote.model

data class Details(
    val fsq_id: String,
    val name: String,
    val description: String?,
    val tel: String?,
    val location: Location,
    val rating: Float?,
    val photos: List<Photo>?,
    val categories: List<Categories>?,
)