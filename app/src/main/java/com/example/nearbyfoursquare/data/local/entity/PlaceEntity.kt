package com.example.nearbyfoursquare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val distance: Int,
    val address: String? = null,
    val country: String? = null,
    val dma: String? = null,
    val region: String? = null,
    val categoryName: String? = null,
    val categoryIcon: String? = null,
)