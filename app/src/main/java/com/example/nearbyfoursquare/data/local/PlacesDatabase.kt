package com.example.nearbyfoursquare.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nearbyfoursquare.data.local.dao.PlacesDao
import com.example.nearbyfoursquare.data.local.entity.PlacesEntity

@Database(
    entities = [PlacesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao
}