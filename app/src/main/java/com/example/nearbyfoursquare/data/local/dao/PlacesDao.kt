package com.example.nearbyfoursquare.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nearbyfoursquare.data.local.entity.PlacesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(repositories: List<PlacesEntity>)

    @Query("SELECT * FROM places")
    fun getAllPlaces(): Flow<List<PlacesEntity>>

    @Query("DELETE FROM places")
    suspend fun clearAll()
}