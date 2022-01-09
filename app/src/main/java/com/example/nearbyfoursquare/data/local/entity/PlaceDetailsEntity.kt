package com.example.nearbyfoursquare.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nearbyfoursquare.data.remote.model.Photo

@Entity(tableName = "places_details")
data class PlaceDetailsEntity(
    @PrimaryKey
    val fsqId: String,
    val name: String,
    val description: String? = null,
    val tel: String? = null,
    val address: String? = null,
    val rating: Float? = null,
    val photo: String? = null,
)