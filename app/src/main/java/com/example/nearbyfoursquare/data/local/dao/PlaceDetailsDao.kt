package com.example.nearbyfoursquare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyfoursquare.data.local.entity.PlaceDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlace(placeDetailsEntity: PlaceDetailsEntity): Long

    @Query("SELECT * FROM places_details WHERE fsqId = :id")
    fun getPlaceDetails(id: String): Flow<PlaceDetailsEntity>

    @Query("DELETE FROM places_details WHERE fsqId = :id")
    suspend fun deleteById(id: String)
}