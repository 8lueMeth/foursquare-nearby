package com.example.nearbyfoursquare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nearbyfoursquare.data.local.dao.PlaceDetailsDao
import com.example.nearbyfoursquare.data.local.dao.PlacesDao
import com.example.nearbyfoursquare.data.local.entity.PlaceDetailsEntity
import com.example.nearbyfoursquare.data.local.entity.PlaceEntity

@Database(
    entities = [PlaceEntity::class, PlaceDetailsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao
    abstract fun placeDetailsDao(): PlaceDetailsDao
}